package com.vocalabs.egtest.processor.data;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/** Describes one example or other item needed for constructing tests.  */
public interface EgItem<T extends Annotation> {
    T getAnnotation();
    Element getElement();
}
