package com.vocalabs.egtest.processor.data;

import com.vocalabs.egtest.processor.AnnotationCollector;
import com.vocalabs.egtest.processor.MessageHandler;
import com.vocalabs.egtest.processor.selftest.ExpectedBehavior;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

public interface AnnotationReader<E extends Example<?>> {

    default void addExamples(RoundEnvironment roundEnvironment, AnnotationCollector collector, boolean selfTest) {
        MessageHandler messageHandler = collector.getMessageHandler();
        for (Class<? extends Annotation> annotationClass: supportedAnnotationClasses()) {
            for (Element el: roundEnvironment.getElementsAnnotatedWith(annotationClass)) {
                ExpectedBehavior expectedBehavior = SelfTestExample.expectedBehavior(el, selfTest, messageHandler);
                for (E example: examples(el.getAnnotation(annotationClass), el, messageHandler)) {
                    collector.add( SelfTestExample.decorate(example, expectedBehavior ));
                }
            }
        }
    }

    /** Every annotation has exactly one factory to handle it. */
    Set<Class<? extends Annotation>> supportedAnnotationClasses();

    /**
     * Checks to see if this element has a matching examples.
     * @param annotation must be a supported annotation type
     */
    List<E> examples(Annotation annotation, Element element, MessageHandler messageHandler);
}
