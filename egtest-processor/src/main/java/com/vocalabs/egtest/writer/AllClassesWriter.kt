package com.vocalabs.egtest.writer

import com.vocalabs.egtest.annotation.EgLanguage
import com.vocalabs.egtest.processor.AnnotationCollector
import com.vocalabs.egtest.processor.Settings
import com.vocalabs.egtest.writer.junit.JUnitClassWriter
import com.vocalabs.egtest.writer.kotlin.KotlinFileWriter

import java.io.File
import java.io.IOException

/** Build test source code; this is the code generator entry point. */
class AllClassesWriter(private val defaultLanguage: EgLanguage,
                       private val directoryExistsBehavior: Settings.AlreadyExistsBehavior,
                       private val directoryToFill: File) : EgTestWriter {

    @Throws(Exception::class)
    override fun write(annotationCollector: AnnotationCollector) {
        if (Settings.AlreadyExistsBehavior.FAIL == directoryExistsBehavior && directoryToFill.exists()) {
            annotationCollector.messageHandler
                    .error("EgTest target directory exists (${directoryToFill.absolutePath})")
            return
        }

        val didCreateDir = directoryToFill.mkdirs()
        if (Settings.AlreadyExistsBehavior.DELETE == directoryExistsBehavior && !didCreateDir) {
            val files = directoryToFill.listFiles()
                    ?: throw IOException("Location for writing EgTest source is not a directory: $directoryToFill")
            files.forEach { f -> deleteRecursive(f) }
        }

        val messageHandler = annotationCollector.messageHandler
        val itemsByClassName = annotationCollector.itemsByClassName
        for ((className, items) in itemsByClassName) {
            when(annotationCollector.languageForClassName.getOrDefault(className, defaultLanguage)) {
                EgLanguage.KOTLIN -> KotlinFileWriter.write(className, messageHandler, items, directoryToFill)
                else -> JUnitClassWriter
                        .write(annotationCollector.languageForClassName,
                                defaultLanguage,
                                className,
                                messageHandler,
                                items,
                                directoryToFill)
            }

        }
    }

    private fun deleteRecursive(file: File) {
        file.listFiles()?.forEach { deleteRecursive(it) }
        file.delete()
    }
}
