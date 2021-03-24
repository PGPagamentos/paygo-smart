package br.com.paygo.smart.model.print

import android.content.Context
import android.util.Log
import br.com.setis.printer.IPrinter
import br.com.setis.printer.IPrinterListener
import br.com.setis.printer.PrinterError

class Print: PrinterIntention, IPrinterListener {
    override fun print(iPrinter: IPrinter, content: String) {
        iPrinter.printLine(content, this)
    }

    override fun printFormfeed(iPrinter: IPrinter) {
        iPrinter.printFormFeed(this)
        iPrinter.printFormFeed(this)
        iPrinter.printFormFeed(this)
        iPrinter.printFormFeed(this)
    }

    override fun onSuccess() {
        Log.i(PRINT_CLASSNAME, "Impressão feita com sucesso")
    }

    override fun onError(error: PrinterError?) {
        Log.i(PRINT_CLASSNAME, "Impressão não realizada $error")
    }

    companion object {
        const val PRINT_CLASSNAME = "Print.kt"
    }
}