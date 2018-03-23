package com.vocalabs.egtest.codegenerator
import java.io.File
import kotlin.reflect.KType
import kotlin.collections.List
import kotlin.*

abstract class AbstractSourceFileBuilder : SourceFileBuilder, AbstractCodeBuilding() {
    var classes: List<ClassBuild> = listOf()

    override fun addClass(name: String, properties: List<KType>): ClassBuilder {
        val cl = ClassBuild(name, properties)
        cl.addClass()
        classes += cl
        return cl
    }

    fun buildString(): String {
        val functionStr: String = functions.joinToString("\n") { it.build() }
        val classesString = classes.joinToString("\n") { it.build() }
        return listOf(imports, classesString, functionStr).joinToString("\n\n")
    }
}

class PrintingSourceFileBuilder(): AbstractSourceFileBuilder() {
    override fun build() {
        println(buildString())
    }
}

class FileSourceFileBuilder(val fileName: String): AbstractSourceFileBuilder() {
    override fun build() {
        File(fileName).bufferedWriter().use { out -> out.write(buildString()) }
    }
}

class StringSourceFileBuilder(): AbstractSourceFileBuilder() {


    override fun build() {
        throw UnsupportedOperationException("Call buildString instead")
    }

    override fun toString(): String {
        return buildString()
    }
}