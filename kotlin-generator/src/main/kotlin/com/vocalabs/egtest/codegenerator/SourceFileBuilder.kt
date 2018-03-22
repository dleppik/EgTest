package com.vocalabs.egtest.codegenerator

import kotlin.reflect.KType

/** Write Kotlin source code files. */
interface SourceFileBuilder : CodeBuilding {

    /** Create a class. The properties are used in the constructor. */
    fun addClass(name: String, properties: List<KType>): ClassBuilder

    /**
     * Write the code. This should only be called once, and the "add" methods should not be used
     * after this has been called.
     */
    fun build()
}