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
}