package com.vocalabs.egtest.codegenerator

fun annotationToString(annotationName: String, annotationBody: String?): String {
    if (annotationBody == null) {
        return "@$annotationName"
    }else {
        return "@$annotationName($annotationBody)"
    }
}