package com.vocalabs.egtest.processor.data;

import com.vocalabs.egtest.annotation.EgDefaultLanguage;
import com.vocalabs.egtest.annotation.EgLanguage;
import com.vocalabs.egtest.processor.MessageHandler;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DefaultLanguageReader implements AnnotationReader<EgItem<EgDefaultLanguage>> {

    public static final DefaultLanguageReader INSTANCE = new DefaultLanguageReader();

    @Override
    public Set<Class<? extends Annotation>> supportedAnnotationClasses() {
        return Collections.singleton(EgDefaultLanguage.class);
    }

    @Override
    public List<EgItem<EgDefaultLanguage>> examples(Annotation annotation, Element element, MessageHandler messageHandler) {
        return Collections.singletonList(new Item(element, (EgDefaultLanguage) annotation));
    }

    public static class Item implements EgItem<EgDefaultLanguage> {
        private final Element element;
        private final EgDefaultLanguage language;

        public Item(Element element, EgDefaultLanguage language) {
            this.language = language;
            this.element = element;
        }

        @Override
        public EgDefaultLanguage getAnnotation() {
            return language;
        }

        @Override
        public Element getElement() {
            return element;
        }
    }
}
