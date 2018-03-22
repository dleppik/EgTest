package com.vocalabs.egtest.codegenerator

import org.junit.Test
import kotlin.test.*

class CodeBuilderTest {

    /** Test that complete source code can be generated.
     * Unfortunately, this test will need to be changed when
     * CodeBuilder classes are changed even when they produce valid code.
     */
    @Test
    fun completeSourceCodeCanBeGenerated() {
        val expected =
                """com.vocalabs.hello.*
                    |com.vocalabs.goodbye.AuRevoir
                    |
                    |fun greet(): String {
                    |    return "hello"
                    |}
                    |
                    |class HelloTest() {
                    |    testGreeting() {
                    |       assertEquals("hello", Greeter.DEFAULT_GREETING)
                    |       println("got here")
                    |       println("also got here")
                    |    }
                    |}
                    |""".trimMargin()

        val cb: CodeBuilder = StringCodeBuilder()

        cb.addImport("com.vocalabs.hello.*")

        val greetFunction = cb.addFunction("greet", listOf(), stringKType)
        greetFunction.addLines("return \"hello\"")

        val testClass = cb.addClass("HelloTest", listOf())

        val testGreeting: FunctionBuilder = testClass.addFunction("testGreeting", listOf(), voidKType)
        testGreeting.addLines("assertEquals(\"hello\", Greeter.DEFAULT_GREETING)")
        testGreeting.addLines(
            """println("got here")
            |println("also got here")
            """.trimMargin())
        cb.addImport("com.vocalabs.goodbye.AuRevoir")

        assertEquals(expected.simplifyWhitespace(), cb.toString().simplifyWhitespace())
    }

    /** Contains a complete test of Kotlin code building. */
    @Test
    fun completeCodeExampleCanBeBuilt() {
        val expected =
                """package com.vocalabs.egtest.example
                    |
                    |import javax.annotation.Generated
                    |import org.junit.Test
                    |import kotlin.test.*
                    |import com.vocalabs.egtest.annotation.*
                    |
                    |@Generated("com.vocalabs.egtest.EgTest")
                    |class Example() {
                    |    @Test
                    |    fun returnsGreet() {
                    |        assertEquals("Hello, World!", Example.greet("World"))
                    |    }
                    |}
                """.trimMargin()

        fail("Annotations aren't supported yet")
    }

    private fun String.simplifyWhitespace(): String {
            return this.replace("([\t ])+", " ")
                    .replace("\n+", "\n")
    }


    private fun sampleFunctionA(a: String) {}
    private fun sampleFunctionB(): String = ""
    private val stringKType = ::sampleFunctionB.returnType
    private val voidKType = ::sampleFunctionA.returnType
}