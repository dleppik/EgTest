package com.vocalabs.egtest.codegenerator
import java.io.File
import kotlin.collections.List
import kotlin.*

abstract class AbstractSourceFileBuilder : SourceFileBuilder, AbstractCodeBuilding() {
    private var classes: List<ClassBuilderImpl> = listOf()
    private var packages: String = ""

    override fun addPackage(name: String) {
        packages += "package $name"
    }

    override fun addClass(name: String): ClassBuilder {
        val cl = ClassBuilderImpl(name)
        classes += cl
        return cl
    }

    fun buildString(): String {
        val functionStr: String = functions.joinToString("\n") { it.build() }
        val classesString = classes.joinToString("\n") { it.build() }
        return listOf(packages, imports, functionStr, classesString).joinToString("\n\n")
    }
}

class FileSourceFileBuilder(private val file: File): AbstractSourceFileBuilder() {
    override fun build() {
        file.bufferedWriter().use { out -> out.write(buildString()) }
    }
}

class StringSourceFileBuilder: AbstractSourceFileBuilder() {
    override fun build() {
        throw UnsupportedOperationException("Call buildString instead")
    }

    override fun toString(): String {
        return buildString()
    }
}