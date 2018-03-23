package com.vocalabs.egtest.codegenerator

interface ClassBuilder: CodeBuilding {

    fun annotationToString(annotationName: String, annotationBody: String): String {
       return "@$annotationName($annotationBody)"
    }

    // TODO Uncomment and implement
    // fun addAnnotation(annotationName: String, annotationBody: String)
}