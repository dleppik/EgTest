package com.vocalabs.egtest.writer.kotlin

import com.vocalabs.egtest.annotation.EgMatch
import com.vocalabs.egtest.codegenerator.ClassBuilder
import com.vocalabs.egtest.codegenerator.FileSourceFileBuilder
import com.vocalabs.egtest.processor.MessageHandler
import com.vocalabs.egtest.processor.data.*
import com.vocalabs.egtest.writer.Constants
import java.io.File
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier

/** Write unit test classes. */
object KotlinFileWriter {
    @JvmStatic
    fun write(
            classToTestName: String,
            messageHandler: MessageHandler,
            itemsForClass: List<EgItem<*>>,
            targetDirectory: File) {
        val egSuffix = "EgTest"
        val file = File(targetDirectory.toString() + "/" + classToTestName.replace('.', '/') + egSuffix + ".kt")
        val parent: File = file.parentFile!!
        parent.mkdirs()

        val (packageName, simpleClassToTestName) = splitClassName(classToTestName)

        val fb = FileSourceFileBuilder(file, packageName)
        addImports(fb)


        val cb = fb.addClass(simpleClassToTestName+egSuffix)
        cb.addAnnotation("Generated", "\"${Constants.GENERATED_BY}\"")

        val itemsByElement = itemsForClass.groupBy { it.element }

        for ((element: Element, itemsForElement) in itemsByElement) {
            writeMatchExample(element, itemsForElement.filterIsInstance<MatchExample>(), cb, simpleClassToTestName)
        }
        fb.build()
    }


    private fun writeMatchExample(element: Element, egs: List<MatchExample>, cb: ClassBuilder, classToTestName: String) {
        val (matches, noMatches) = egs.partition { it.annotation is EgMatch }
        writeMatchExample(element, "assertTrue", "EgMatch", matches, cb, classToTestName)
        writeMatchExample(element, "assertFalse", "EgNoMatch", noMatches, cb, classToTestName)
    }

    private fun writeMatchExample(element: Element,
                                  assertion: String,
                                  matchType: String,
                                  egs: List<MatchExample>,
                                  cb: ClassBuilder,
                                  classToTestName: String) {
        if (egs.isEmpty()) {
            return
        }
        val testName = "${element.simpleName}_$matchType"
        val f = cb.addFunction(testName)
        f.addAnnotation("Test")

        val pattern = element.simpleName.toString()
        val patternClassStr = element.asType().toString()

        for (eg in egs) {
            val toMatch = eg.toMatch()
            val matchF = when (eg) {
                is PatternMatchExample -> when (patternClassStr) {
                    "java.util.regex.Pattern" -> ".matcher(\"$toMatch\").matches()"
                    "kotlin.text.Regex" -> ".matches(\"$toMatch\")"
                    else -> throw IllegalArgumentException("Not a pattern: $patternClassStr")
                }
                is FunctionMatchExample -> "(\"$toMatch\")"
                else -> throw IllegalArgumentException("Unknown MatchExample: $eg")
            }
            val constructor = constructorArgs(eg)
            f.addLines("$assertion(\"${eg.toMatch()}\", $classToTestName$constructor.$pattern$matchF)")
        }
    }

    private fun constructorArgs(eg: MatchExample): String {
        return if (eg.element.modifiers.contains(Modifier.STATIC)) ""
        else "(${eg.constructorArgs().joinToString(", ")})"
    }

    private fun splitClassName(classToTestName: String): Pair<String, String> {
        val splitPos = classToTestName.lastIndexOf('.')
        val packageName = classToTestName.substring(0, splitPos)
        val simpleClassName = classToTestName.substring(splitPos + 1)
        return Pair(packageName, simpleClassName)
    }

    private fun addImports(fb: FileSourceFileBuilder) {
        fb.addImport("javax.annotation.Generated")
        fb.addImport("org.junit.Test")
        fb.addImport("org.junit.Assert.*")
    }
}