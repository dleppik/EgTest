package com.vocalabs.egtest.codegenerator

import kotlin.reflect.KType

interface CodeBuilder {
    fun addImport(importName: String)

    fun addFunction(arguments: List<KType>, returnType: KType)

    /** Write the code. */
    fun build()
}