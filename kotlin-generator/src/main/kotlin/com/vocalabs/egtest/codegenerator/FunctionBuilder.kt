package com.vocalabs.egtest.codegenerator

/** Builds a function used within a SourceFileBuilder. */
interface FunctionBuilder {

    /** Add one or more lines to the function. This may be called multiple times. */
    fun addLines(string: String)

    // TODO Uncomment and implement using CodeUtil.annotationToString
    // fun addAnnotation(annotationName: String, annotationBody: String?)
}
