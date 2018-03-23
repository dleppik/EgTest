package com.vocalabs.egtest.codegenerator

// As of March 2018, IntelliJ has better support for org.junit.Assert than kotlin.test.Assert
import org.junit.Assert.*
import org.junit.Ignore
import org.junit.Test

class CodeBuilderTest {

    /** Test that complete source code can be generated.
     * Unfortunately, this test will need to be changed when
     * SourceFileBuilder classes are changed even when they produce valid code.
     */
    @Test
    fun completeSourceCodeCanBeGenerated() {
        val expected =
                """import com.vocalabs.hello.*
                    |import com.vocalabs.goodbye.AuRevoir
                    |
                    |fun greet(): String {
                    |    return "hello"
                    |}
                    |
                    |class HelloTest() {
                    |    fun testGreeting(): Unit {
                    |       assertEquals("hello", Greeter.DEFAULT_GREETING)
                    |       println("got here")
                    |       println("also got here")
                    |    }
                    |}
                    |""".trimMargin()

        val sfb: SourceFileBuilder = StringSourceFileBuilder()

        sfb.addImport("com.vocalabs.hello.*")

        val greetFunction = sfb.addFunction("greet", listOf(), stringKType)
        greetFunction.addLines("return \"hello\"")

        val testClass = sfb.addClass("HelloTest", listOf())

        val testGreeting: FunctionBuilder = testClass.addFunction("testGreeting", listOf(), voidKType)
        testGreeting.addLines("assertEquals(\"hello\", Greeter.DEFAULT_GREETING)")
        testGreeting.addLines(
            """println("got here")
            |println("also got here")
            """.trimMargin())
        sfb.addImport("com.vocalabs.goodbye.AuRevoir")

        assertEquals(expected.simplifyWhitespace(), sfb.toString().simplifyWhitespace())
    }

    /** Contains a complete test of Kotlin code building. */
    @Ignore
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
                    |    @Ignore
                    |    @Test
                    |    fun returnsGreet() {
                    |        assertEquals("Hello, World!", Example.greet("World"))
                    |    }
                    |}
                """.trimMargin()

        fail("Annotations aren't supported yet")
    }

    private fun String.simplifyWhitespace(): String {
            return this.replace("[\t ]*\n+[\t ]*".toRegex(), "\n")
    }

    private fun sampleFunctionA() {}
    private fun sampleFunctionB(): String = ""
    private val stringKType = ::sampleFunctionB.returnType
    private val voidKType = ::sampleFunctionA.returnType
}