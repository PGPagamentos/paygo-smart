package br.com.paygo.smart.model.paygo

import br.com.setis.interfaceautomacao.EntradaTransacao
import br.com.setis.interfaceautomacao.SaidaTransacao
import br.com.setis.interfaceautomacao.StatusTransacao

interface TransactionIntention {
    suspend fun makeTransaction(transactionParams: EntradaTransacao): SaidaTransacao
    suspend fun confirm(status: StatusTransacao, id: String?)
}