package com.vocalabs.egtest.codegenerator

import kotlin.reflect.KType

class FunctionBuild(val name: String, val arguments: List<KType>, val returnType: KType) : FunctionBuilder {
    var annotations : String = ""
    var signature : String = ""
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
            var separatedT = returnType.toString().split(".")
            val TReturn = separatedT[separatedT.lastIndex]
            signature += "): $TReturn"
    }

    override fun addLines(lineToBeAdded: String) {
        functionSoFar = "$functionSoFar\t\t$lineToBeAdded\n"
    }

    override fun addAnnotation(annotationName: String, annotationBody: String?) {
        val annotationStr = annotationToString(annotationName, annotationBody)
        annotations += "$annotationStr\n"
    }

    fun build(): String {
        return "$annotations\n$signature {\n$functionSoFar}"
    }
}