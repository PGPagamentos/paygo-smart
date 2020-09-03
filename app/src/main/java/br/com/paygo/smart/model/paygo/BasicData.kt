package br.com.paygo.smart.model.paygo

import br.com.setis.interfaceautomacao.DadosAutomacao

class BasicData {
    companion object {
        val dados = DadosAutomacao(
            "PayGo Smart",
            "1.0.0",
            "PayGo",
            false,
            false,
            false,
            false
        )
    }
}