package com.vocalabs.egtest.codegenerator

 class ClassBuilderImpl(val name: String): AbstractCodeBuilding(), ClassBuilder {
     private val classSignature: String = "class $name"
     var annotations = listOf<String>()
     var classBody: String= ""

     override fun addAnnotation(annotationName: String, annotationBody: String?) {
         val annotationStr = annotationToString(annotationName, annotationBody)
         annotations += "$annotationStr\n"
     }

     fun build(): String {
         val functionStr: String = functions.joinToString("\n") { it.build() }
         classBody += functionStr
         return "${annotations.joinToString("\n")}$classSignature {\n$classBody\n}\n"
     }
 }
