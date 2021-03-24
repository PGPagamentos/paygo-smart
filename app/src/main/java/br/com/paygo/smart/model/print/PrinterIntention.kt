package br.com.paygo.smart.model.print

import br.com.setis.printer.IPrinter

interface PrinterIntention {
    fun print(iPrinter: IPrinter, content: String)
    fun printFormfeed(iPrinter: IPrinter)
}