package com.vocalabs.egtest.processor.junit;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import com.vocalabs.egtest.annotation.EgMatch;
import com.vocalabs.egtest.annotation.EgNoMatch;
import com.vocalabs.egtest.processor.data.EgItem;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.List;

abstract class MatchWriter<T extends Element, X extends EgItem<?>> extends JUnitExampleWriter<T,X> {
    public MatchWriter(T element, List<X> examples, JUnitClassWriter classWriter, TypeSpec.Builder toAddTo) {
        super(element, examples, classWriter, toAddTo);
    }

    @Override
    protected String baseName() {
        return "Match";
    }

    protected ClassName booleanAssertion(Annotation annotation) {
        final String boolStr;
        if (annotation instanceof EgMatch)
            boolStr = "True";
        else if (annotation instanceof EgNoMatch)
            boolStr = "False";
        else throw new IllegalArgumentException(this + " doesn't handle " + annotation);
        return ClassName.get("org.junit.Assert", "assert" + boolStr);
    }
}
