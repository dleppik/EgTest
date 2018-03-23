package com.vocalabs.egtest.codegenerator

import kotlin.reflect.KType

class FunctionBuild(/*imports: String,*/ val name: String, val arguments: List<KType>, val returnType: KType) : FunctionBuilder {
    var annotation : String = ""
    var signature : String = ""
    var functionSoFar : String = ""

    fun addSignature() {
        signature = "fun $name("
        val listIterator = arguments.iterator()
        var index = 1
        for (t in listIterator) {
            val nextType = listIterator.next()
            signature += "arg$index : $nextType"
            if(index != arguments.lastIndex){
                signature += ", "
            }
            index++
        }
        var separatedT = returnType.toString().split(".")
        val TReturn = separatedT[separatedT.lastIndex]
        signature += "): $TReturn"
    }

    override fun addLines(lineToBeAdded: String) {
        functionSoFar = "$functionSoFar\t$lineToBeAdded\n"
    }

    override fun addAnnotation(annotationName: String, annotationBody: String?) {
        val annotationStr = annotationToString(annotationName, annotationBody)
        annotation += "$annotationStr\n"
    }

    fun build(): String {
        return "$annotation\n$signature {\n$functionSoFar}"
    }
}