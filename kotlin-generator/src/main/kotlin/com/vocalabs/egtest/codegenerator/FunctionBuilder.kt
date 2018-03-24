package com.vocalabs.egtest.codegenerator

/** Builds a function used within a SourceFileBuilder. */
interface FunctionBuilder {

    /** Add one or more lines to the function. This may be called multiple times. */
    fun addLines(lineToBeAdded: String)

    fun addAnnotation(annotationName: String, annotationBody: String? = null)
}
