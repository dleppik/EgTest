package com.vocalabs.egtest.writer.kotlin

import com.vocalabs.egtest.codegenerator.FileSourceFileBuilder
import com.vocalabs.egtest.processor.MessageHandler
import com.vocalabs.egtest.processor.data.EgItem
import java.io.File

/** Write unit test classes. */
class KotlinFileWriter private constructor(classToTestName: String,
                                           messageHandler: MessageHandler,
                                           items: List<EgItem<*>>)
{
    companion object {
        @JvmStatic fun write(
                classToTestName: String,
                messageHandler: MessageHandler,
                items: List<EgItem<*>>,
                targetDirectory: File)
        {
            //
            // TODO  This is example code to show that we have what we need
            // import org.junit.Test
            //import org.junit.Assert.*
            //        import org.junit.Ignore

            //

            val egSuffix = "EgTest"

            val file = File(targetDirectory.toString() +"/" + classToTestName.replace('.', '/') + egSuffix+".kt")
            val parent: File = file.parentFile!!
            parent.mkdirs()


            val classNamePos = classToTestName.lastIndexOf('.')
            val packageName = classToTestName.substring(0, classNamePos)
            val simpleClassName = classToTestName.substring(classNamePos+1)+egSuffix


            val fb = FileSourceFileBuilder(file)
            fb.addPackage(packageName)
            fb.addImport("org.junit.Test")
            fb.addImport("org.junit.Ignore")
            fb.addImport("org.junit.Assert.*")

            val cl = fb.addClass(simpleClassName, listOf())
            val function = cl.addFunction("testSomething", listOf(), unitKType)
            function.addAnnotation("Test", null)
            function.addAnnotation("Ignore", null)
            function.addLines("""fail("Not written")""")
            fb.build()
        }





        private fun example() {}
        private val unitKType = ::example.returnType
    }

}