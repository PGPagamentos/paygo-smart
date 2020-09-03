package br.com.paygo.smart.model.print.ktx

fun List<String>.generateRawReceipt(): String {
    var initialReceipt = ""
    forEach {
       initialReceipt += it
    }
    return initialReceipt
}