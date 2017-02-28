package com.vocalabs.egtest.processor.data;

import javax.lang.model.element.VariableElement;
import java.lang.annotation.Annotation;

public class PatternMatchExample extends MatchExample {
    private final VariableElement element;

    PatternMatchExample(Annotation annotation, VariableElement element) {
        super(annotation);
        this.element = element;
    }

    @Override
    public VariableElement element() { return element; }

}
