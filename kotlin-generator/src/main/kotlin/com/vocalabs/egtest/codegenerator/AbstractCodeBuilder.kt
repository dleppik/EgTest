package com.vocalabs.egtest.codegenerator
import java.io.File
import kotlin.reflect.KType
import kotlin.collections.List
import kotlin.*

abstract class AbstractCodeBuilder: CodeBuilder {

    var giantString : String= ""


    override fun addImport(importName: String){
        giantString += "import $importName\n"
    }

    override fun addClass(name: String, properties: List<KType>): ClassBuilder {
        TODO("not implemented")
    }

    override fun addFunction(name: String, arguments: List<KType>, returnType: KType): FunctionBuilder {
        val listIterator = arguments.iterator()
        var index = 1
        var result = ""
        for (t in listIterator) {
            val nextType = listIterator.next()
            result = result + "arg$index : $nextType"
            if(index != arguments.lastIndex){
                result += ", "
            }
            index++
        }
        giantString += "fun function($result) : $returnType\n"
        TODO()
    }

    fun buildString(): String = giantString
}

class PrintingCodeBuilder(): AbstractCodeBuilder() {
    override fun build() {
        println(buildString())
    }
}

class FileCodeBuilder(val fileName: String): AbstractCodeBuilder() {
    override fun build() {
        File(fileName).bufferedWriter().use { out -> out.write(buildString()) }
    }
}

class StringCodeBuilder(): AbstractCodeBuilder() {
    override fun build() {
        throw UnsupportedOperationException("Call buildString instead")
    }
}