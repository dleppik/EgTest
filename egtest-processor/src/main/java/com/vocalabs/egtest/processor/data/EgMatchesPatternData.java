package com.vocalabs.egtest.processor.data;

import com.vocalabs.egtest.annotation.EgMatches;

import javax.lang.model.element.Element;

public class EgMatchesPatternData implements AnnotationData<EgMatches> {
    private final Element element;
    private final EgMatches annotation;

    public EgMatchesPatternData(EgMatches annotation, Element element) {
        this.element = element;
        this.annotation = annotation;
    }

    public String toMatch() { return annotation.value(); }

    @Override
    public EgMatches annotation() { return annotation; }

    @Override
    public Element element() { return element; }
}
