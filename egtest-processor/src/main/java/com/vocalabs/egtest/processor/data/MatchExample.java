package com.vocalabs.egtest.processor.data;

import com.vocalabs.egtest.annotation.EgMatch;
import com.vocalabs.egtest.annotation.EgNoMatch;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/** Matches and NoMatch. */
public abstract class MatchExample implements EgItem<Annotation> {
    private final Annotation annotation;

    MatchExample(Annotation annotation) {
        this.annotation = annotation;
    }

    public String toMatch() {
        if (annotation instanceof EgMatch)   return ((EgMatch)   annotation).value();
        if (annotation instanceof EgNoMatch) return ((EgNoMatch) annotation).value();
        throw new IllegalArgumentException("Wrong class for "+annotation);
    }

    public List<String> constructorArgs() { return Arrays.asList(constructorArgArray()); }

    private String[] constructorArgArray() {
        if (annotation instanceof EgMatch)   return ((EgMatch)   annotation).construct();
        if (annotation instanceof EgNoMatch) return ((EgNoMatch) annotation).construct();
        throw new IllegalArgumentException("Wrong class for "+annotation);
    }

    @Override
    public Annotation getAnnotation() { return annotation; }
}
