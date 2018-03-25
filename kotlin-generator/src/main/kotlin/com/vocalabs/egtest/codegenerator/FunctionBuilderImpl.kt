package com.vocalabs.egtest.codegenerator

import kotlin.reflect.KType

class FunctionBuilderImpl(val name: String, val arguments: List<KType>, val returnType: KType) : FunctionBuilder {
    var annotations = listOf<String>()
    var signature: String = ""
    var functionSoFar : String = ""

    fun addSignature() {
            signature = "fun $name("
            val listIterator = arguments.iterator()
            var index = 1
            for (t in listIterator) {
                val nextType = listIterator.next()
                signature += "arg$index : $nextType"
                if (index != arguments.lastIndex) {
                    signature += ", "
                }
                index++
            }
            val separatedT = returnType.toString().split(".")
            val TReturn = separatedT[separatedT.lastIndex]
            signature += "): $TReturn"
    }

    override fun addLines(lineToBeAdded: String) {
        functionSoFar = "$functionSoFar\t\t$lineToBeAdded\n"
    }

    override fun addAnnotation(annotationName: String, annotationBody: String?) {
        val annotationStr = annotationToString(annotationName, annotationBody)
        annotations += annotationStr
    }

    fun build(): String {
        return "${annotations.joinToString("\n")}\n$signature {\n$functionSoFar}"
    }
}