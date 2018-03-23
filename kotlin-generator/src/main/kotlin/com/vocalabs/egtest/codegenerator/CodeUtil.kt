package com.vocalabs.egtest.codegenerator

fun annotationToString(annotationName: String, annotationBody: String?): String {
    val bodyStr: String = annotationBody ?: ""
    return "@$annotationName($bodyStr)"
}