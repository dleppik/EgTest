package com.vocalabs.egtest.processor.junit;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

/**
 * Convenience methods for java.lang.model.*.
 */
public class JavaModelUtil {

    /**
     * Find the outermost TypeElement (class or interface) which is or contains this element; in Java, this
     * corresponds to the source file where this was defined.
     */
    public static TypeElement topLevelClass(Element element) {
        return topLevelClass(element, element);
    }

    private static TypeElement topLevelClass(Element element, Element initialElement) {
        if (element == null || element instanceof PackageElement) {
            throw new IllegalArgumentException("No enclosing class: "+initialElement);
        }
        Element parent = element.getEnclosingElement();
        if (element instanceof TypeElement) {
            if (parent == null || parent instanceof PackageElement)
                return (TypeElement) element;
        }
        return topLevelClass(parent, initialElement);
    }
}
