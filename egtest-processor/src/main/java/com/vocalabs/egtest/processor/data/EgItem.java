package com.vocalabs.egtest.processor.data;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

/** Describes one example or other item needed for constructing tests.  */
public interface EgItem<T extends Annotation> {
    T getAnnotation();
    Element getElement();
}
