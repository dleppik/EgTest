package com.vocalabs.egtest.processor.data;

import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;

public class FunctionMatchExample extends MatchExample {
    private final ExecutableElement element;

    FunctionMatchExample(Annotation annotation, ExecutableElement element) {
        super(annotation);
        this.element = element;
    }

    @Override
    public ExecutableElement element() {
        return element;
    }
}
