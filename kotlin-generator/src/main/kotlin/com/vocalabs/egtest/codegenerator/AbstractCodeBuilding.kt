package com.vocalabs.egtest.codegenerator

import kotlin.reflect.KType

/** Contains shared methods for CodeBuilding */
abstract class AbstractCodeBuilding : CodeBuilding {
    var imports: String = ""
    var functions: List<FunctionBuild> = listOf()

    override fun addImport(importName: String){
        imports += "import $importName\n"
    }

    override fun addFunction(name: String, arguments: List<KType>, returnType: KType): FunctionBuilder {
        var toReturn = FunctionBuild(name, arguments, returnType)
        functions += toReturn
        return toReturn
    }

}