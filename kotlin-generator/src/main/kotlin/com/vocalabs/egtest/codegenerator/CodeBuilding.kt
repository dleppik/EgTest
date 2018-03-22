package com.vocalabs.egtest.codegenerator

import kotlin.reflect.KType

/** Generates Kotlin which can contain imports and functions. */
interface CodeBuilding {

    /** Add a top-level import statement which can be used by any function. */
    fun addImport(importName: String)

    /** Create a FunctionBuilder which will be used by the SourceFileBuilder to write a function. */
    fun addFunction(name: String, arguments: List<KType>, returnType: KType): FunctionBuilder
}