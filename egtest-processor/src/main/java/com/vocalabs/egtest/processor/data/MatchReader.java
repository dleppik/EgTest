package com.vocalabs.egtest.processor.data;

import com.vocalabs.egtest.annotation.EgMatches;
import com.vocalabs.egtest.annotation.EgMatchesContainer;
import com.vocalabs.egtest.annotation.EgNoMatch;
import com.vocalabs.egtest.annotation.EgNoMatchContainer;
import com.vocalabs.egtest.processor.MessageHandler;

import javax.lang.model.element.*;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MatchReader implements AnnotationReader<MatchExample> {
    public static final MatchReader INSTANCE = new MatchReader();

    @Override
    public Set<Class<? extends Annotation>> supportedAnnotationClasses() {
        return Stream.of(
                EgMatches.class,
                EgNoMatch.class,
                EgMatchesContainer.class,
                EgNoMatchContainer.class)
                .collect(Collectors.toSet());
    }

    @Override
    public List<MatchExample> examples(Annotation annotation, Element element, MessageHandler messageHandler) {
        if (annotation instanceof EgMatches || annotation instanceof EgNoMatch) {
            return example(annotation, element, messageHandler);
        }
        if (annotation instanceof EgMatchesContainer) {
            return Arrays.stream(((EgMatchesContainer) annotation).value())
                    .flatMap(egMatches -> example(egMatches, element, messageHandler).stream())
                    .collect(Collectors.toList());
        }
        if (annotation instanceof EgNoMatchContainer) {
            return Arrays.stream(((EgNoMatchContainer) annotation).value())
                    .flatMap(egMatches -> example(egMatches, element, messageHandler).stream())
                    .collect(Collectors.toList());
        }
        throw new IllegalArgumentException("Unsupported annotation: "+annotation);
    }

    private List<MatchExample> example(Annotation annotation, Element element, MessageHandler messageHandler) {
        if (isPattern(element)) {
            return Collections.singletonList(new PatternMatchExample(annotation, (VariableElement) element));
        }
        else if (element instanceof ExecutableElement) {
            return Collections.singletonList(new FunctionMatchExample(annotation, (ExecutableElement) element));
        }
        messageHandler.unsupported(element, "neither Pattern nor method");
        return Collections.emptyList();
    }

    private boolean isPattern(Element element) {
        return element instanceof VariableElement
                && element.getKind().equals(ElementKind.FIELD)
                && element.asType().toString().equals("java.util.regex.Pattern");
    }
}
