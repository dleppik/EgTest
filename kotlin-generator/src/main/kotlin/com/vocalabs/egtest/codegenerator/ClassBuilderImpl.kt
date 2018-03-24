package com.vocalabs.egtest.codegenerator

import kotlin.reflect.KType

 class ClassBuilderImpl(val name: String): AbstractCodeBuilding(), ClassBuilder {
     private val classSignature: String = "class $name"
     var annotations: String = ""
     var listofFunctions: List<FunctionBuild> = listOf()
     var classBody: String= ""

     override fun addFunction(name: String, arguments: List<KType>, returnType: KType): FunctionBuilder {
         val function = FunctionBuild(name, arguments, returnType)
         function.addSignature()
         listofFunctions += function
         return function
     }

     override fun addAnnotation(annotationName: String, annotationBody: String?) {
         val annotationStr = annotationToString(annotationName, annotationBody)
         annotations += "$annotationStr\n"
     }

     fun build(): String {
         val functionStr: String = listofFunctions.joinToString("\n\t") { it.build() }
         classBody += functionStr
         return "$annotations$classSignature {\n$classBody\n}\n"
     }
 }
