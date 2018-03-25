package com.vocalabs.egtest.processor

import com.vocalabs.egtest.annotation.EgMatch
import com.vocalabs.egtest.annotation.EgMatchContainer
import javax.annotation.processing.Messager
import javax.annotation.processing.RoundEnvironment
import javax.tools.Diagnostic

object AnnotationAnalyzer {
    @JvmStatic
    fun findExamples(roundEnvironment: RoundEnvironment, messager: Messager) {
        roundEnvironment.getElementsAnnotatedWith(EgMatch::class.java).forEach { el ->
            messager.printMessage(Diagnostic.Kind.NOTE, "Single annotation")
            describeMatch(el.getAnnotation(EgMatch::class.java), messager)
        }
        roundEnvironment.getElementsAnnotatedWith(EgMatchContainer::class.java).forEach { el ->
            val annotations = el.getAnnotation(EgMatchContainer::class.java).value
            messager.printMessage(Diagnostic.Kind.NOTE, "Got ${annotations.size} annotations")
            annotations.forEach { describeMatch(it, messager) }
        }
    }

    private fun describeMatch(egMatch: EgMatch, messager: Messager) {
        messager.printMessage(Diagnostic.Kind.NOTE,
                "Got EgMatch value={${egMatch.value}} a=[${egMatch.construct.joinToString(",")}]")
    }
}