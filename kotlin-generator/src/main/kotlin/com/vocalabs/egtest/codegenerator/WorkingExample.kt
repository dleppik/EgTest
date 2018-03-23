package com.vocalabs.egtest.codegenerator

import java.io.File

fun main(argv: Array<String>) {
    val builder = FileSourceFileBuilder("kotlin-generator/src/main/kotlin.com/vocalabs/egtest/Generated.kt")
    val f = builder.addFunction("greet", listOf(), stringKType)
    f.addLines("""return "Hello, World!" """)
    builder.build()
}

private fun sampleFunctionA() {}
private fun sampleFunctionB(): String = ""
private val stringKType = ::sampleFunctionB.returnType
private val voidKType = ::sampleFunctionA.returnType