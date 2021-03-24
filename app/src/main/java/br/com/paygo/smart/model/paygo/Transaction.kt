package br.com.paygo.smart.model.paygo

import android.content.Context
import br.com.setis.interfaceautomacao.*

class Transaction(context: Context) : TransactionIntention {
    private val transaction: Transacao by lazy {
        Transacoes.obtemInstancia(
            BasicData.dados,
            context
        )
    }

    override suspend fun makeTransaction(transactionParams: EntradaTransacao): SaidaTransacao =
        transaction.realizaTransacao(transactionParams)

    override suspend fun confirm(
        status: StatusTransacao,
        id: String?
    ) {
        val confirmacao = Confirmacoes().apply {
            informaStatusTransacao(status)
            informaIdentificadorConfirmacaoTransacao(id)
        }
        transaction.confirmaTransacao(confirmacao)
    }

    /**
     * O método a seguir mostra como realizar a operação de confirmação ou desfasimento de uma venda pendente.
     * Os status que pode ser usados são:
     * CONFIRMADO_MANUAL;
     * O status CONFIRMADO_MANUAL deve ser atribuido quando a venda deve ser confirmada
     * DESFEITO_MANUAL
     * O status DESFEITO_MANUAL deve ser atribuido quando a venda deve ser desfeita.
     */
    override suspend fun confirmPendingTransaction(pendenteDados: TransacaoPendenteDados){
        val confirmacao = Confirmacoes()
        confirmacao.informaStatusTransacao(StatusTransacao.CONFIRMADO_MANUAL)

        transaction.resolvePendencia(pendenteDados, confirmacao)
    }

}