package com.vocalabs.egtest.codegenerator
import kotlin.reflect.KType
import kotlin.collections.List
import kotlin.reflect.KClass

fun f(a: Int): Int  {
    return 1
}

fun main(args: Array<String>) {

    val kType: KType = ::f.returnType

    val example = CodeBuilderImplementation("file.txt")
    example.addImport("kotlin.reflect.KType")
    val listOfTypes : List<KType> = listOf(kType, kType)
    example.addFunction(listOfTypes, kType)
    example.build()
}