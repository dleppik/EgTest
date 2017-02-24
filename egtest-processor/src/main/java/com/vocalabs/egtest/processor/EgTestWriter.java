package com.vocalabs.egtest.processor;

/** Write unit tests once we have collected all the data. */
public interface EgTestWriter {
    enum AlreadyExistsBehavior { OVERWRITE, DELETE, FAIL }

    void write(AnnotationCollector annotationCollector) throws Exception;
}
