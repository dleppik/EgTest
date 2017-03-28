package com.vocalabs.egtest.processor.data;

import com.squareup.javapoet.TypeName;
import com.vocalabs.egtest.annotation.EgException;

import javax.lang.model.element.ExecutableElement;

public class ExceptionExample implements EgItem<EgException> {
    private final ExecutableElement element;
    private final EgException annotation;
    private final TypeName exceptionType;

    public ExceptionExample(ExecutableElement element, EgException annotation, TypeName exceptionType) {
        this.element = element;
        this.annotation = annotation;
        this.exceptionType = exceptionType;
    }

    /** Because the annotation usually contains a TypeMirror, use this instead of {@code getAnnotation().willThrow()} */
    public TypeName exceptionType() {
        return exceptionType;
    }

    @Override public EgException getAnnotation() { return annotation; }
    @Override public ExecutableElement getElement() { return element; }
}
