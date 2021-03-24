package br.com.paygo.smart.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import br.com.paygo.smart.R
import br.com.paygo.smart.model.paygo.Transaction
import br.com.paygo.smart.model.paygo.TransactionIntention
import br.com.paygo.smart.model.print.Print
import br.com.paygo.smart.model.print.PrinterIntention
import br.com.paygo.smart.model.print.ktx.generateRawReceipt
import br.com.setis.interfaceautomacao.EntradaTransacao
import br.com.setis.interfaceautomacao.ModalidadesPagamento
import br.com.setis.interfaceautomacao.Operacoes
import br.com.setis.interfaceautomacao.StatusTransacao
import br.com.setis.printer.IPrinter
import br.com.setis.printer.PrinterManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {
    private val transaction: TransactionIntention by lazy { Transaction(this) }
    private val printer: PrinterIntention by lazy { Print() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Para que a impressora seja agnóstica ao hardware, o printerModule deve ser enviado vazio:
        val iPrint: IPrinter = PrinterManager.getPrinter(this, "")

        /**
         * Instalação
         */
        buttonInstall.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    //Informa que realizaremos uma instalação
                    val entradaTransacao = EntradaTransacao(
                        Operacoes.INSTALACAO, UUID.randomUUID().toString())

                    //Informa o cnpj do estabelecimento comercial:
                    entradaTransacao.informaEstabelecimentoCNPJouCPF("05471416000101")//05471416000101

                    val saidaTransacao = transaction.makeTransaction(entradaTransacao)
                    println(saidaTransacao.obtemMensagemResultado())
                }
            }
        }

        /**
         * Menu administrativo
         */
        buttonConfig.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val saidaTransacao = transaction.makeTransaction(
                        EntradaTransacao(Operacoes.ADMINISTRATIVA, UUID.randomUUID().toString()))
                    println(saidaTransacao.obtemMensagemResultado())
                }
            }
        }

        /**
         * Venda
         */
        buttonSale.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {

                    val entradaTransacao = EntradaTransacao(Operacoes.VENDA, UUID.randomUUID().toString())
                    /**
                     * O valor deve ser enviado com os centavos concatenados, nesse exemplo é uma
                     * venda de R$20,00. Se fosse uma venda de R$25,76, deveríamos enviar:
                     * entradaTransacao.informaValorTotal("2576")
                     */
                    entradaTransacao.informaValorTotal("2000")

                    /**
                    * Caso deseje realizar uma transação PIX, é necessário informar a modalidade de
                    * pagamento como carteira virtual. Para isso basta descomentar o a linha abaixo (linha 86):
                    *
                    * entradaTransacao.informaModalidadePagamento(ModalidadesPagamento.PAGAMENTO_CARTEIRA_VIRTUAL)
                    */

                    val saidaTransacao = transaction.makeTransaction(entradaTransacao)

                    /**
                     * Após a realização de uma venda, é necessário verificar se essa é uma transação
                     * que exige confirmação, através da função saidaTransacao.obtemInformacaoConfirmacao()
                     * Caso essa função retorne true, realizar a confirmação ou desfazimento da venda.
                     */
                    if(saidaTransacao.obtemInformacaoConfirmacao()) {
                        transaction.confirm(StatusTransacao.CONFIRMADO_AUTOMATICO, saidaTransacao.obtemIdentificadorConfirmacaoTransacao())
                        printer.print(iPrint, saidaTransacao.obtemComprovanteCompleto().generateRawReceipt())
                        //Pula linha após a impressão:
                        printer.printFormfeed(iPrint)
                    }
                    /**
                     * Verifica se existe transação pendente e, se sim, confirma ou desfaz:
                     */
                    else if(saidaTransacao.existeTransacaoPendente()){
                        transaction.confirmPendingTransaction(saidaTransacao.obtemDadosTransacaoPendente()!!)
                    }

                    /**
                     * A função saidaTransacao.obtemMensagemResultado() exibe a mensagem retornada
                     * como resultado para a transação. Essa mensagem é importante, especialmente em
                     * caso de erro, pois descreve o motivo pelo qual a venda não foi aprovada.
                     */
                    println(saidaTransacao.obtemMensagemResultado())
                }
            }
        }
    }
}