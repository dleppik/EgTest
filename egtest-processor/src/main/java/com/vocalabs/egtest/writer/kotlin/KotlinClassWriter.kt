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
import javax.lang.model.element.TypeElement

/** Write unit test classes. */
class KotlinFileWriter(private val classToTestName: String,
                       private val messageHandler: MessageHandler,
                       private val itemsForClass: List<EgItem<*>>,
                       private val targetDirectory: File) {
    companion object {
        @JvmStatic
        fun write(
                classToTestName: String,
                messageHandler: MessageHandler,
                itemsForClass: List<EgItem<*>>,
                targetDirectory: File) {
            KotlinFileWriter(classToTestName, messageHandler, itemsForClass, targetDirectory).write()
        }
    }

    fun write() {
        val egSuffix = "EgTest"
        val file = File(targetDirectory.toString() + "/" + classToTestName.replace('.', '/') + egSuffix + ".kt")
        val parent: File = file.parentFile!!
        parent.mkdirs()

        val (packageName, simpleClassToTestName) = splitClassName(classToTestName)

        val builder = FileSourceFileBuilder(file, packageName)
        addImports(builder)

        val cb = builder.addClass(simpleClassToTestName+egSuffix)
        cb.addAnnotation("javax.annotation.Generated", "\"${Constants.GENERATED_BY}\"")

        val itemsByElement = itemsForClass.groupBy { it.element }

        for ((el: Element, itemsForElement) in itemsByElement) {
            writeMatchExample(    el, itemsForElement.filterIsInstance<MatchExample>(),     cb)
            writeReturnsExample(  el, itemsForElement.filterIsInstance<ReturnsExample>(),   cb)
            writeExceptionExample(el, itemsForElement.filterIsInstance<ExceptionExample>(), cb)
        }
        builder.build()
    }

    private fun addImports(fb: FileSourceFileBuilder) {
        fb.addImport("org.junit.Test")
        fb.addImport("org.junit.Assert.*")  // We use JUnit because kotlin.assert doesn't have assertEquals with delta
    }

    private fun writeMatchExample(element: Element, egs: List<MatchExample>, cb: ClassBuilder) {
        val (matches, noMatches) = egs.partition { it.annotation is EgMatch }
        writeMatchExample(element, "assertTrue", "EgMatch", matches, cb)
        writeMatchExample(element, "assertFalse", "EgNoMatch", noMatches, cb)
    }

    private fun writeMatchExample(element: Element,
                                  assertion: String,
                                  matchType: String,
                                  egs: List<MatchExample>,
                                  cb: ClassBuilder) {
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
            val functionName = fullFunctionName(eg, "$pattern$matchF")
            f.addLines("$assertion(\"${eg.toMatch()}\", $functionName)")
        }
    }

    private fun writeReturnsExample(element: Element, egs: List<ReturnsExample>, cb: ClassBuilder) {
        if (egs.isEmpty()) {
            return
        }
        val testName = "${element.simpleName}_Eg"
        val f = cb.addFunction(testName)
        f.addAnnotation("Test")

        for (eg in egs) {
            val given = eg.annotation.given.joinToString(", ")
            val returns = eg.annotation.returns
            val description = "(${eg.annotation.given.joinToString(", ")}) -> $returns".replace("\"", "\\\"")
            val functionName = fullFunctionName(eg, element.simpleName.toString())
            val comparison = when (eg) {
                is ReturnsWithDeltaExample -> "$returns, $functionName($given), ${deltaString(eg)}"
                else -> "$returns, $functionName($given)"
            }
            f.addLines("assertEquals(\"$description\", $comparison)")
        }
    }

    private fun writeExceptionExample(element: Element, egs: List<ExceptionExample>, cb: ClassBuilder) {
        if (egs.isEmpty()) {
            return
        }
        val testName = "${element.simpleName}_Eg"
        val f = cb.addFunction(testName)
        f.addAnnotation("Test")

        for (eg in egs) {
            val exTypeName: String = when(eg.exceptionType().toString()) {
                "java.lang.Throwable" -> "Throwable"  // Default type for annotation
                else -> eg.exceptionType().toString()
            }
            val given = eg.annotation.value.joinToString(", ")
            val functionName = fullFunctionName(eg, element.simpleName.toString())
            f.addLines(
                    """try {
                      |     $functionName($given)
                      |     fail("should throw $exTypeName when given $given")
                      |}
                      |catch (_: $exTypeName) { }
                      |""".trimMargin()
            )
        }

        // Could use kotin.test.assertFails() or assertFailsWith()
        // or wrap in try/catch
        // TODO()
    }

    private fun deltaString(eg: ReturnsWithDeltaExample): String {
        val returnType = eg.element.returnType.toString()
        val delta = eg.annotation.delta
        val suffix = when (returnType) {
            "double" -> ""
            "java.lang.Double" -> ""
            "float" -> delta.toString() + "f"
            "java.lang.Float" -> delta.toString() + "f"
            else -> throw IllegalArgumentException("Not a floating-point return type: $returnType")
        }
        return delta.toString() + suffix
    }

    private fun fullFunctionName(eg: Constructing<*>, simpleFunctionName: String): String {
        return if (isTopLevel(eg.element)) simpleFunctionName
               else "$classToTestName${constructorArgs(eg)}.$simpleFunctionName"
    }

    private fun constructorArgs(eg: Constructing<*>): String {
        return if (isStatic(eg.element)) ""
        else "(${eg.constructorArgs().joinToString(", ")})"
    }

    /**
     * Detect elements (especially functions) which are called by Kotlin source code without naming an enclosing class.
     * Unfortunately this can only be done by a heuristic which looks for "Kt" at the end of the class name.
     */
    private fun isTopLevel(element: Element): Boolean {
        val parent = element.enclosingElement
        return parent is TypeElement && isKotlinTopLevelContainer(parent)
    }

    private fun isKotlinTopLevelContainer(element: TypeElement): Boolean {
        return isKotlinClass(element) && element.simpleName.toString().endsWith("Kt")
    }

    /** Check for Java static or Kotlin singleton.
     * An imperfect heuristic: if not static, checks for a static INSTANCE field. */
    private fun isStatic(element: Element): Boolean {
        if (element.modifiers.contains(Modifier.STATIC)) return true
        else {
            val parent = element.enclosingElement
            if (parent is TypeElement && isKotlinClass(parent)) {
                val instances = parent.enclosedElements
                        .filter { "INSTANCE" == it.simpleName.toString() }
                        .filter { it.modifiers.contains(Modifier.STATIC) }
                        .filter { it.kind.isField }
                return instances.isNotEmpty()
            }
            return false
        }
    }

    /**
     * Detect Kotlin class using private metadata check; there is no guarantee
     * that this will continue to work for future Kotlin versions.
     */
    // TODO support @file:JvmName()
    private fun isKotlinClass(el: TypeElement)
            = el.annotationMirrors.any { it.annotationType.toString() == "kotlin.Metadata" }

    private fun splitClassName(classToTestName: String): Pair<String, String> {
        val splitPos = classToTestName.lastIndexOf('.')
        val packageName = classToTestName.substring(0, splitPos)
        val simpleClassName = classToTestName.substring(splitPos + 1)
        return Pair(packageName, simpleClassName)
    }
}