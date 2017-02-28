package com.vocalabs.egtest.processor.data;

import com.vocalabs.egtest.annotation.EgMatches;
import com.vocalabs.egtest.annotation.EgNoMatch;

import java.lang.annotation.Annotation;

/** Matches and NoMatch. */
public abstract class MatchExample implements Example<Annotation> {
    private final Annotation annotation;

    MatchExample(Annotation annotation) {
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
}
