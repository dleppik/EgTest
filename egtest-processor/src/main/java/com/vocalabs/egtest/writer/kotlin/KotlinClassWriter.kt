package com.vocalabs.egtest.writer.kotlin

import com.vocalabs.egtest.annotation.EgMatch
import com.vocalabs.egtest.annotation.EgNoMatch
import com.vocalabs.egtest.codegenerator.ClassBuilder
import com.vocalabs.egtest.codegenerator.FileSourceFileBuilder
import com.vocalabs.egtest.processor.MessageHandler
import com.vocalabs.egtest.processor.data.EgItem
import com.vocalabs.egtest.processor.data.IgnoredReader
import com.vocalabs.egtest.processor.data.MatchExample
import com.vocalabs.egtest.processor.data.PatternMatchExample
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
            writePatternMatch(element, itemsForElement.filterIsInstance<PatternMatchExample>(), cb, simpleClassToTestName)
        }

        fb.build()
    }


    private fun writePatternMatch(element: Element, egs: List<PatternMatchExample>, cb: ClassBuilder, classToTestName: String) {
        val (matches, noMatches) = egs.partition { it.annotation is EgMatch }

        writePatternMatch(element, "assertTrue", "EgMatch", matches, cb, classToTestName)
        writePatternMatch(element, "assertFalse", "EgNoMatch", noMatches, cb, classToTestName)
    }

    private fun writePatternMatch(element: Element,
                                  assertion: String,
                                  matchType: String,
                                  egs: List<PatternMatchExample>,
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
            val matchF = when (patternClassStr) {
                "java.util.regex.Pattern" -> "matcher(\"${eg.toMatch()}\").matches()"
                "kotlin.text.Regex" -> "matches(\"${eg.toMatch()}\")"
                else -> throw IllegalArgumentException("Not a pattern: $patternClassStr")
            }
            val constructor = constructorArgs(eg)
            f.addLines("// ${eg.toMatch()} -- ${args(eg.annotation).asList()}")
            f.addLines("$assertion(\"${eg.toMatch()}\", $classToTestName$constructor.$pattern.$matchF)")
        }
    }

    private fun constructorArgs(eg: MatchExample): String {
        return when (eg.element.modifiers.contains(Modifier.STATIC)) {
            true -> ""
            false -> {
                val argList = args(eg.annotation)
                "(${argList.joinToString(", ")})"
            }
        }
    }

    private fun args(a: Annotation): Array<String> {
        return when (a) {
            is EgMatch -> a.construct
            is EgNoMatch -> a.construct
            else -> arrayOf()
        }
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