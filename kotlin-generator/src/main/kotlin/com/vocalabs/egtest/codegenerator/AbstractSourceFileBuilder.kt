package com.vocalabs.egtest.codegenerator
import java.io.File
import kotlin.collections.List
import kotlin.*

abstract class AbstractSourceFileBuilder(private val packageStr: String) : SourceFileBuilder, AbstractCodeBuilding() {
    private var classes: List<ClassBuilderImpl> = listOf()
    private var imports: String = ""

    override fun addImport(importName: String){
        imports += when {
            imports.isEmpty() -> "import $importName"
            else -> "\nimport $importName"
        }
    }

    override fun addClass(name: String): ClassBuilder {
        val cl = ClassBuilderImpl(name)
        classes += cl
        return cl
    }

    fun buildString(): String {
        val functionStr: String = functions.joinToString("\n") { it.build() }
        val classesString = classes.joinToString("\n") { it.build() }
        return listOf("package $packageStr", imports, functionStr, classesString).joinToString("\n\n")
    }
}

class FileSourceFileBuilder(private val file: File, packageStr: String): AbstractSourceFileBuilder(packageStr) {
    override fun build() {
        file.bufferedWriter().use { out -> out.write(buildString()) }
    }
}

class StringSourceFileBuilder(packageStr: String): AbstractSourceFileBuilder(packageStr) {
    override fun build() {
        throw UnsupportedOperationException("Call buildString instead")
    }

    override fun toString(): String {
        return buildString()
    }
}