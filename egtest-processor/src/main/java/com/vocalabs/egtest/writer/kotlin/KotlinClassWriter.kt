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
import java.io.File
import javax.lang.model.element.Modifier

/** Write unit test classes. */
object KotlinFileWriter {
    @JvmStatic
    fun write(
            classToTestName: String,
            messageHandler: MessageHandler,
            items: List<EgItem<*>>,
            targetDirectory: File) {
        val egSuffix = "EgTest"
        val file = File(targetDirectory.toString() + "/" + classToTestName.replace('.', '/') + egSuffix + ".kt")
        val parent: File = file.parentFile!!
        parent.mkdirs()

        val (packageName, simpleClassToTestName) = splitClassName(classToTestName)

        val fb = FileSourceFileBuilder(file, packageName)
        addImports(fb)


        val cb = fb.addClass(simpleClassToTestName+egSuffix)

        /*
        val function = cl.addFunction("testSomething", listOf(), unitKType)
        function.addAnnotation("Test", null)
        function.addAnnotation("Ignore", null)
        function.addLines("""fail("Not written")""")
        */
        //
        // TODO  This is example code to show that we have what we need
        //

        for (item in items) {
            when (item) {
                is PatternMatchExample -> writePatternMatch(item, cb, simpleClassToTestName)
                is IgnoredReader -> {
                }
                else -> TODO()
            }
        }

        fb.build()
    }


    private fun writePatternMatch(eg: PatternMatchExample, cb: ClassBuilder, classToTestName: String) {
        val (assertion, matchType) = when (eg.annotation) {
            is EgMatch -> Pair("assertTrue", "EgMatch")
            is EgNoMatch -> Pair("assertFalse", "EgNoMatch")
            else -> throw IllegalArgumentException("Unsupported annotation: ${eg.annotation}")
        }

        if (eg.element.modifiers.contains(Modifier.STATIC)) {
            val testName = "test_"+eg.element.simpleName+"_"+matchType // TODO Better naming
            val f = cb.addFunction(testName)
            f.addAnnotation("Test")

            val pattern = eg.element.simpleName.toString()

            val patternClassStr = eg.element.asType().toString()
            val matchF = when (patternClassStr) {
                "java.util.regex.Pattern" -> ".matcher(\"${eg.toMatch()}\").matches()"
                "kotlin.text.Regex" -> "matches(\"${eg.toMatch()}\")"
                else -> throw IllegalArgumentException("Not a pattern: $patternClassStr")
            }
            f.addLines("$assertion(\"${eg.toMatch()}\", $classToTestName.$pattern.$matchF)")
        }
        else {
            TODO()
        }
    }


    private fun splitClassName(classToTestName: String): Pair<String, String> {
        val splitPos = classToTestName.lastIndexOf('.')
        val packageName = classToTestName.substring(0, splitPos)
        val simpleClassName = classToTestName.substring(splitPos + 1)
        return Pair(packageName, simpleClassName)
    }

    private fun addImports(fb: FileSourceFileBuilder) {
        fb.addImport("org.junit.Test")
        fb.addImport("org.junit.Ignore")
        fb.addImport("org.junit.Assert.*")
    }
}