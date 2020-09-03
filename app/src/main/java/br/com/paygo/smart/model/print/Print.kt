package br.com.paygo.smart.model.print

import android.content.Context
import android.util.Log
import br.com.setis.printer.IPrinter
import br.com.setis.printer.IPrinterListener
import br.com.setis.printer.PrinterError
import br.com.setis.printer_newland.PrinterN910

class Print(private val context: Context) : PrinterIntention, IPrinterListener {
    private val printerNewland: IPrinter by lazy {
        PrinterN910(context)
    }

    override fun print(content: String) {
        printerNewland.printLine(content, this)
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