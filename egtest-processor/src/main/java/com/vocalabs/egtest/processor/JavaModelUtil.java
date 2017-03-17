package com.vocalabs.egtest.processor;

import javax.lang.model.element.*;

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

    /** Returns a String which should uniquely describe this element within the source code. */
    public static String key(Element el) {
        if (el instanceof QualifiedNameable) {
            return ((QualifiedNameable) el).getQualifiedName().toString();
        }
        Element parent = el.getEnclosingElement();
        final String base;
        if (parent instanceof QualifiedNameable)
            base = key(parent);
        else if (parent == el)
            base = "##Self##";
        else
            base = "##Unknown "+parent.toString()+"##";

        if (el instanceof VariableElement) {
            String kindName = typeKey((VariableElement) el);
            return kindName+" "+base+"#"+el.getSimpleName();
        }
        if (el instanceof ExecutableElement) {
            ExecutableElement ee = (ExecutableElement) el;
            String params = ee.getParameters()
                    .stream()
                    .map(JavaModelUtil::typeKey)
                    .reduce((a,b) -> a+", "+b)
                    .orElse("");
            return base+"#"+ee.getSimpleName()+"("+params+")";
        }
        throw new IllegalArgumentException("Don't know how to describe "+el);
    }

    private static String typeKey(VariableElement el) {
        return el.asType().toString();
    }
}
