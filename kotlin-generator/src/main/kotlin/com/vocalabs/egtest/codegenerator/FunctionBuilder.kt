package com.vocalabs.egtest.codegenerator

/** Builds a function used within a CodeBuilder. */
interface FunctionBuilder {

    /** Add one or more lines to the function. This may be called multiple times. */
    fun addLines(string: String)
}
