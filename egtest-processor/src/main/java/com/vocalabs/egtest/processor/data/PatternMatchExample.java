package com.vocalabs.egtest.processor.data;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

public class PatternMatchExample extends MatchExample {
    PatternMatchExample(Annotation annotation, Element element) {
        super(annotation, element);
    }
}
