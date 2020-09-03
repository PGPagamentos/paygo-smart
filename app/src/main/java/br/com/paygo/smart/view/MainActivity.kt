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
import br.com.setis.interfaceautomacao.Operacoes
import br.com.setis.interfaceautomacao.StatusTransacao
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {
    private val transaction: TransactionIntention by lazy { Transaction(this) }
    private val printer: PrinterIntention by lazy { Print(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonConfig.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val result = transaction.makeTransaction(EntradaTransacao(Operacoes.ADMINISTRATIVA, UUID.randomUUID().toString()))
                    /*
                        obtemInformacaoConfirmacao() só virá 'true' quando a operação for venda
                        Recomend. fazer nada se vier 'false'
                    */
                    println(result.obtemInformacaoConfirmacao())
                }
            }
        }

        buttonSale.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val transactionWithSale = EntradaTransacao(Operacoes.VENDA, UUID.randomUUID().toString()).apply {
                        informaValorTotal("1000")
                    }
                    transaction.makeTransaction(transactionWithSale).also {
                        if(it.obtemInformacaoConfirmacao()) {
                            transaction.confirm(StatusTransacao.CONFIRMADO_AUTOMATICO, it.obtemIdentificadorConfirmacaoTransacao())
                            printer.print(it.obtemComprovanteCompleto().generateRawReceipt())
                        }
                    }
                }
            }
        }
    }
}