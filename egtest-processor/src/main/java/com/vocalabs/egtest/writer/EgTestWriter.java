package com.vocalabs.egtest.writer;

import com.vocalabs.egtest.processor.AnnotationCollector;

/** Writes the unit tests; rather than read annotations directly, uses an {@link AnnotationCollector}. */
public interface EgTestWriter {

    void write(AnnotationCollector annotationCollector) throws Exception;
}
