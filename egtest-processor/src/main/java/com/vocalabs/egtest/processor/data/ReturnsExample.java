package com.vocalabs.egtest.processor.data;

import com.vocalabs.egtest.annotation.Eg;

import javax.lang.model.element.ExecutableElement;

/** Eg tests of the form {@code  @Eg(given = {"1", "2"}, returns = "3") } */
public class ReturnsExample implements Example<Eg> {
    private final ExecutableElement element;
    private final Eg annotation;

    public ReturnsExample(ExecutableElement element, Eg annotation) {
        this.element = element;
        this.annotation = annotation;
    }

    @Override public Eg getAnnotation() { return annotation; }
    @Override public ExecutableElement getElement() { return element; }
}
