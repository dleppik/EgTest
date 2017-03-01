package com.vocalabs.egtest.processor.data;


import com.vocalabs.egtest.annotation.*;
import com.vocalabs.egtest.processor.MessageHandler;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Keep track of annotations we haven't written support for yet; will be deleted once we have Readers for all our Annotations. */
@Deprecated
public class NotSupportedReader implements AnnotationReader<Example<?>> {

    public static final NotSupportedReader INSTANCE = new NotSupportedReader();

    @Override
    public Set<Class<? extends Annotation>> supportedAnnotationClasses() {
        return Stream.of(
                EgException.class,
                EgExceptionContainer.class)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Example<?>> examples(Annotation annotation, Element element, MessageHandler messageHandler) {
        messageHandler.notYetSupported(element, annotation);
        return Collections.emptyList();
    }
}
