package com.vocalabs.egtest.processor.data;

import com.vocalabs.egtest.annotation.Eg;

import javax.lang.model.element.ExecutableElement;

/**
 * Indicates that the return type is an inexact numerical type (float, Float, double, or Double) AND the expected
 * result is not null.
 * Therefore tests require a delta (plus or minus) to indicate the range of acceptable results.
 */
public class ReturnsWithDeltaExample extends ReturnsExample {
    public ReturnsWithDeltaExample(ExecutableElement element, Eg annotation) {
        super(element, annotation);
    }

    /** Formats the delta as a float or double based on the return type. */
    public String deltaString() {
        String returnType = getElement().getReturnType().toString();
        double delta = getAnnotation().delta();
        switch (returnType) {
            case "double":           return delta+"d";
            case "java.lang.Double": return delta+"d";
            case "float":            return delta+"f";
            case "java.lang.Float":  return delta+"f";
            default: throw new IllegalArgumentException("Not a floating-point return type: "+returnType);
        }
    }
}
