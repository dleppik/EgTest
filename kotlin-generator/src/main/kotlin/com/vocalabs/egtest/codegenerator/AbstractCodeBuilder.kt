package com.vocalabs.egtest.codegenerator
import java.io.File
import kotlin.reflect.KType
import kotlin.collections.List
import kotlin.*

abstract class AbstractCodeBuilder: CodeBuilderInterface {

    var giantString : String= ""


    override fun addImport(importName: String){
        giantString += "import $importName\n"
    }

    override fun addFunction(arguments: List<KType>, returnType: KType){
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
    }

    fun buildString(): String = giantString

    //override fun build() {
    //    File(fileName).bufferedWriter().use { out -> out.write(giantString) }
    //}
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

class UnitTestCodeBuilder(): AbstractCodeBuilder() {
    override fun build() {
        throw UnsupportedOperationException("Not used in unit tests")
    }
}