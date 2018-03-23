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
            val cb = FileSourceFileBuilder("")

            TODO()
        }
    }


}