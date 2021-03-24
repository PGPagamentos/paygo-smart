package br.com.paygo.smart.model.paygo

import br.com.setis.interfaceautomacao.DadosAutomacao
import br.com.setis.interfaceautomacao.Personalizacao

class BasicData {
    companion object {
        val dados = DadosAutomacao(
            "PayGo Smart",
            "1.0.1",
            "PayGo",
            false,
            false,
            false,
            false,
            personalizaApp()
        )

        /**
         * Função responsável pela personalização do PG Integrado.
         * É necessário criar um Personalizacao.Builder() e enviar a cor em hex desejada para cada
         * campo que queira trocar de cor.
         */
        private fun personalizaApp():Personalizacao{
            val pb = Personalizacao.Builder()
            pb.informaCorFundoTela("#787878")
            pb.informaCorFundoTeclado("#363636")

            return pb.build()
        }
    }
}