package com.vocalabs.egtest.codegenerator
import java.io.File
import kotlin.reflect.KType
import kotlin.collections.List
import kotlin.*

class CodeBuilderImplementation(val fileName: String) : CodeBuilderInterface {

    var giantString : String= ""


    override fun addImport(importName: String){
        giantString += "import $importName\n"
    }

    override fun addFunction(arguments: List<KType>, returnType: KType){
        val listIterator = arguments.iterator()
        var index = 1
        var result = ""
        for (t in listIterator) {
            var nextType = listIterator.next()
            nextType = nextType.substring(6)
            result = result + "arg$index : $nextType"
            if(index != arguments.lastIndex){
                result += ", "
            }
            index++
        }
        giantString += "fun function($result) : $returnType\n"
    }

    override fun build() {
        File(fileName).bufferedWriter().use { out -> out.write(giantString) }
    }


}