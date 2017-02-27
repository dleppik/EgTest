package com.vocalabs.egtest.processor.data;

import com.vocalabs.egtest.annotation.EgMatches;
import com.vocalabs.egtest.annotation.EgNoMatch;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

public class PatternMatchExample extends MatchExample<Annotation> {
    private final Element element;
    private final Annotation annotation;

    PatternMatchExample(Annotation annotation, Element element) {
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
