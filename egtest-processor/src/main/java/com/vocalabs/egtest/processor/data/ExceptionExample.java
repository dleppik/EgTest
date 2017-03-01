package com.vocalabs.egtest.processor.data;

import com.vocalabs.egtest.annotation.EgException;

import javax.lang.model.element.ExecutableElement;

public class ExceptionExample implements Example<EgException> {
    private final ExecutableElement element;
    private final EgException annotation;

    public ExceptionExample(ExecutableElement element, EgException annotation) {
        this.element = element;
        this.annotation = annotation;
    }

    @Override public EgException getAnnotation() { return annotation; }
    @Override public ExecutableElement getElement() { return element; }
}
