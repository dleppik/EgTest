package com.vocalabs.egtest.processor.data;


import com.vocalabs.egtest.processor.MessageHandler;
import com.vocalabs.egtest.processor.selftest.EgSelfTest;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Annotations for internal use. */
public class SelfTestReader implements AnnotationReader<Example<?>> {

    public static final SelfTestReader INSTANCE = new SelfTestReader();

    @Override
    public Set<Class<? extends Annotation>> supportedAnnotationClasses() {
        return Stream.of(EgSelfTest.class).collect(Collectors.toSet());
    }

    @Override
    public List<Example<?>> examples(Annotation annotation, Element element, MessageHandler messageHandler) {
        return Collections.emptyList();
    }
}
