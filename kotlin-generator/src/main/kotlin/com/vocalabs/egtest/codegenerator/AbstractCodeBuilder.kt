package com.vocalabs.egtest.codegenerator
import java.io.File
import kotlin.reflect.KType
import kotlin.collections.List
import kotlin.*

abstract class AbstractCodeBuilder: CodeBuilder, AbstractCodeBuilding() {
    override fun addClass(name: String, properties: List<KType>): ClassBuilder {
        return ClassBuild(name, properties)
    }
}

class PrintingCodeBuilder(): AbstractCodeBuilder() {
    override fun build() {
        println(buildString())
    }
}

class FileCodeBuilder(val fileName: String): AbstractCodeBuilder() {
    override fun build() {
        File(fileName).bufferedWriter().use { out -> out.write(buildString()) }
    }
}

class StringCodeBuilder(): AbstractCodeBuilder() {


    override fun build() {
        throw UnsupportedOperationException("Call buildString instead")
    }

    override fun toString(): String {
        return buildString()
    }
}