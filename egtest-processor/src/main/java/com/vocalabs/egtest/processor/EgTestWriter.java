package com.vocalabs.egtest.processor;

/** Writes the unit tests; rather than read annotations directly, uses an {@link AnnotationCollector}. */
public interface EgTestWriter {

    void write(AnnotationCollector annotationCollector) throws Exception;
}
