package com.vocalabs.egtest.processor.data;

import com.vocalabs.egtest.annotation.Eg;
import com.vocalabs.egtest.annotation.EgContainer;
import com.vocalabs.egtest.processor.MessageHandler;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Handles {@link com.vocalabs.egtest.annotation.Eg}.
 */
public class ReturnsReader implements AnnotationReader<ReturnsExample> {
    public static final ReturnsReader INSTANCE = new ReturnsReader();

    private static final Set<Class<? extends Annotation>>
            ANNOTATION_CLASSES = Stream.of(
                    Eg.class,
                    EgContainer.class)
                    .collect(Collectors.toSet());

    private static final Set<String>
            DELTA_RETURN_TYPES = Stream.of(
                    "float",
                    "java.lang.Float",
                    "double",
                    "java.lang.Double")
                    .collect(Collectors.toSet());

    @Override
    public Set<Class<? extends Annotation>> supportedAnnotationClasses() { return ANNOTATION_CLASSES; }

    @Override
    public List<ReturnsExample> examples(Annotation annotation, Element element, MessageHandler messageHandler) {
        if ( ! (element instanceof ExecutableElement)) {
            messageHandler.unsupported(element, annotation, "non-executable");
            return Collections.emptyList();
        }

        ExecutableElement el = (ExecutableElement) element;

        if (annotation instanceof Eg) {
            return Collections.singletonList(example(el, (Eg) annotation));
        }
        if (annotation instanceof EgContainer) {
            return Arrays.stream(((EgContainer)annotation).value())
                    .map(eg -> example(el, eg))
                    .collect(Collectors.toList());
        }
        throw new IllegalArgumentException("Unsupported annotation: "+annotation);
    }

    private ReturnsExample example(ExecutableElement el, Eg eg) {
        if (DELTA_RETURN_TYPES.contains(el.getReturnType().toString())  &&  ! "null".equals(eg.returns().trim()))
            return new ReturnsWithDeltaExample(el, eg);
        return new ReturnsExample(el, eg);
    }
}
