package com.vocalabs.egtest.processor.data;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/** Describes one example from an annotation.  */
public interface Example<T extends Annotation> {
    T annotation();
    Element element();

    default boolean visible() {
        Set<Modifier> modifiers = element().getModifiers();
        return modifiers.contains(Modifier.PUBLIC) || modifiers.contains(Modifier.DEFAULT);
    }

    default boolean isStatic() {
       return element().getModifiers().contains(Modifier.STATIC);
    }

}
