package com.vocalabs.egtest.codegenerator

import kotlin.reflect.KType

/** Contains shared methods for CodeBuilding */
abstract class AbstractCodeBuilding : CodeBuilding {
    var functions: List<FunctionBuilderImpl> = listOf()

    override fun addFunction(name: String, arguments: List<KType>, returnType: KType): FunctionBuilder {
        val toReturn = FunctionBuilderImpl(name, arguments, returnType)
        toReturn.addSignature()
        functions += toReturn
        return toReturn
    }
}