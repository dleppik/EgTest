package com.vocalabs.egtest.codegenerator

interface ClassBuilder: CodeBuilding {
    fun addAnnotation(annotationName: String, annotationBody: String?)
}