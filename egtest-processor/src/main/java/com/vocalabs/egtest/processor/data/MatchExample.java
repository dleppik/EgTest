package com.vocalabs.egtest.processor.data;

import com.vocalabs.egtest.annotation.EgMatches;
import com.vocalabs.egtest.annotation.EgMatchesContainer;
import com.vocalabs.egtest.annotation.EgNoMatch;
import com.vocalabs.egtest.annotation.EgNoMatchContainer;
import com.vocalabs.egtest.processor.MessageHandler;

import javax.lang.model.element.*;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Matches and NoMatch. */
public abstract class MatchExample implements Example<Annotation> {
    private final Element element;
    private final Annotation annotation;

    MatchExample(Annotation annotation, Element element) {
        this.element = element;
        this.annotation = annotation;
    }

    public String toMatch() {
        if (annotation instanceof EgMatches)
            return ((EgMatches) annotation).value();
        if (annotation instanceof EgNoMatch)
            return ((EgNoMatch) annotation).value();
        throw new IllegalArgumentException("Wrong class for "+annotation);
    }

    @Override
    public Annotation annotation() { return annotation; }

    @Override
    public Element element() { return element; }

}
