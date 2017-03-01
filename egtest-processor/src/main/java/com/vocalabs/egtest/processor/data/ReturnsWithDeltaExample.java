package com.vocalabs.egtest.processor.data;

import com.vocalabs.egtest.annotation.Eg;

import javax.lang.model.element.ExecutableElement;

/**
 * Indicates that the return type is an inexact numerical type: float, Float, double, or Double. Therefore tests
 * require a delta (plus or minus) to indicate the range of acceptable results.
 */
public class ReturnsWithDeltaExample extends ReturnsExample {
    public ReturnsWithDeltaExample(ExecutableElement element, Eg annotation) {
        super(element, annotation);
    }
}
