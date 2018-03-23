package com.vocalabs.egtest.codegenerator

import kotlin.reflect.KType

/** Contains shared methods for CodeBuilding */
abstract class AbstractCodeBuilding : CodeBuilding {
    var imports: String = ""
    var functions: List<FunctionBuild> = listOf()
    //var classes: List<ClassBuild> = listOf()

    override fun addImport(importName: String){
        if (imports.length == 0) {
            imports += "import $importName"
        }else{
            imports += "\nimport $importName"
        }
    }

    override fun addFunction(name: String, arguments: List<KType>, returnType: KType): FunctionBuilder {
        var toReturn = FunctionBuild(name, arguments, returnType)
        toReturn.addSignature()
        functions += toReturn
        return toReturn
    }


}