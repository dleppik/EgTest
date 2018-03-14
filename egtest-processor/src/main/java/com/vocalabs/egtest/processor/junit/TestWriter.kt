package com.vocalabs.egtest.processor.junit


import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeSpec
import com.vocalabs.egtest.processor.MessageHandler
import com.vocalabs.egtest.processor.data.*
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

abstract class TestWriter<out T : Element, out X : EgItem<*>>
protected constructor(protected val element: T,
                      protected val examples: List<X>,
                      protected val classWriter: ClassWriter,
                      protected val toAddTo: TypeSpec.Builder)
{
    protected val messageHandler: MessageHandler = classWriter.messageHandler
    protected val testAnnotation: AnnotationSpec = AnnotationSpec.builder(ClassName.get("org.junit", "Test")).build()

    abstract fun addTests()

    /**
     * Return false if work needs to be done on this element.
     * If the element is unsupported, return true after alerting messageHandler.
     */
    protected fun notSupported(): Boolean {
        if (examples.isEmpty())
            return true

        if (inInnerClass()
                && !element.modifiers.contains(Modifier.STATIC)
                && !enclosingClass().modifiers.contains(Modifier.STATIC)) {
            messageHandler.unsupported(element, "non-static inner class")
            return true
        }

        if (!visible()) {
            messageHandler.unsupported(element, "private or protected")
            return true
        }
        return false
    }

    private fun visible(): Boolean {
        val modifiers = element.modifiers
        return !modifiers.contains(Modifier.PRIVATE) && !modifiers.contains(Modifier.PROTECTED)
    }

    private fun enclosingClass(): TypeElement {
        var el: Element = element
        while (el !is TypeElement) {
            el = el.enclosingElement
        }
        return el
    }

    private fun inInnerClass(): Boolean {
        return element.enclosingElement != classWriter.classElement
    }

    private fun innerClassNamePortion(): String {
        val outerEl = classWriter.classElement
        val sb = StringBuilder()
        var el = element.enclosingElement
        while (el != outerEl) {
            sb.append("$")
            sb.append(uniqueName(el))
            el = el.enclosingElement
        }
        return sb.toString()
    }

    protected fun testMethodName(): String {
        return "test" + baseName() + innerClassNamePortion() + "$" + uniqueName(element)
    }

    /** The distinctive portion of the name constructed by [.testMethodName].   */
    protected abstract fun baseName(): String

    companion object {

        @JvmStatic
        fun write(classWriter: ClassWriter, builder: TypeSpec.Builder) {
            val examplesByElement = classWriter.items.groupBy { it.element }

            for ((el, examples) in examplesByElement) {

                PatternMatchWriter(el, filterAndConvert(PatternMatchExample::class.java, examples), classWriter, builder)
                        .addTests()
                FunctionMatchWriter(el, filterAndConvert(FunctionMatchExample::class.java, examples), classWriter, builder)
                        .addTests()

                if (el is ExecutableElement) {
                    val returnsExamples = filterAndConvert(ReturnsExample::class.java, examples)
                    EgWriter(el, returnsExamples, classWriter, builder)
                            .addTests()

                    val exceptionExamples = filterAndConvert(ExceptionExample::class.java, examples)
                    ExceptionWriter(el, exceptionExamples, classWriter, builder)
                            .addTests()
                }
            }
        }

        private fun <X : EgItem<*>> filterAndConvert(cl: Class<X>, items: List<Any>): List<X> {
            return items.filter { cl.isAssignableFrom(it.javaClass) }
                    .map({ cl.cast(it) })
        }

        private fun uniqueName(el: Element): String {
            val simpleName = el.simpleName.toString()
            return if (nameCollisions(el).isEmpty()) simpleName else elementUniqueSuffix(el)
        }

        private fun nameCollisions(el: Element): List<Element> {
            return el.enclosingElement
                    .enclosedElements
                    .filter { it -> it != el }
                    .filter { it -> it.simpleName.toString() == el.simpleName.toString() }
                    .map { it -> it as Element }
        }

        private fun elementUniqueSuffix(el: Element): String {
            if (el is ExecutableElement) {
                val params = el.parameters
                        .map { p -> p.asType().toString() }
                return escapeParameterNames(params)
            }
            return stripGenerics("$" + el.kind)
        }

        @JvmStatic
        fun escapeParameterNames(names: Collection<String>): String {
            return "$" +
                    names
                    .map { stripGenerics(it) }
                    .map { it.replace("$", "$$") }
                    .map { it.replace("_", "__") }
                    .map { it.replace(".", "_") }
                    .map { it.replace("[]", "\$_A") }
                    .joinToString("$")
        }

        private fun stripGenerics(s: String): String {
            return s.replace("<.*?>".toRegex(), "")
        }
    }
}
