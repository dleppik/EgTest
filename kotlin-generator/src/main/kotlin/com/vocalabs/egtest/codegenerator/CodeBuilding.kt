package com.vocalabs.egtest.codegenerator

import kotlin.reflect.KType

/** Generates Kotlin which can contain imports and functions. */
interface CodeBuilding {

    /** Create a FunctionBuilder which will be used by the SourceFileBuilder to write a function. */
    fun addFunction(name: String, arguments: List<KType> = listOf(), returnType: KType = unitKType): FunctionBuilder
}

private fun example() {}
val unitKType = ::example.returnType
