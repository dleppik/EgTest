package com.vocalabs.egtest.codegenerator
import kotlin.reflect.KType
import kotlin.collections.List

fun f(): Int  {
    return 1
}

fun l() : List<Int>{
    return listOf(1)
}

fun main(args: Array<String>) {

    val kType: KType = ::f.returnType
    val kType2: KType = ::l.returnType

    val example = PrintingCodeBuilder()
    example.addImport("kotlin.reflect.KType")
    val listOfTypes : List<KType> = listOf(kType, kType2)
    example.addFunction(listOfTypes, kType)
    example.build()
}