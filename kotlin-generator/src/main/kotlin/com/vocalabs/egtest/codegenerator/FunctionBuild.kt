package com.vocalabs.egtest.codegenerator

import kotlin.reflect.KType

class FunctionBuild(/*imports: String,*/ val name: String, val arguments: List<KType>, val returnType: KType) : FunctionBuilder {
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
        signature += ") : $returnType\n"
    }

    override fun addLines(lineToBeAdded: String) {
        functionSoFar += lineToBeAdded
    }

    fun build(): String {
        return "the text of the function should be returned here"
    }
}