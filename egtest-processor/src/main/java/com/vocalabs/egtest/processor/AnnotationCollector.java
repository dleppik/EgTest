package com.vocalabs.egtest.processor;


import com.vocalabs.egtest.annotation.EgLanguage;
import com.vocalabs.egtest.processor.data.DefaultLanguageReader;
import com.vocalabs.egtest.processor.data.EgItem;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.*;

public class AnnotationCollector {
    private final Map<String, EgLanguage> languageForClassName = new HashMap<>();
    private final Map<String, List<EgItem<?>>> itemsByClassName = new TreeMap<>();
    private final MessageHandler messageHandler;

    public AnnotationCollector(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void add(EgItem<?> data) {
        if (data instanceof DefaultLanguageReader.Item)
            addDefaultLanguage(data);
        else
            addExample(data);
    }

    private void addExample(EgItem<?> data) {
        Element classEl = JavaModelUtil.topLevelClass(data.getElement());
        if (classEl.getKind().equals(ElementKind.CLASS)) {
            String name = className(classEl);
            itemsByClassName.computeIfAbsent(name, x -> new ArrayList<>())
                    .add(data);
        }
        else {
            messageHandler.error("Container is not a class: "+data.getElement());
        }
    }

    private void addDefaultLanguage(EgItem<?> data) {
        Element classEl = data.getElement();
        if (classEl.equals(JavaModelUtil.topLevelClass(data.getElement()))) {
            EgLanguage language = ((DefaultLanguageReader.Item)data).getAnnotation().value();
            languageForClassName.put(className(classEl), language);
        }
        else {
            messageHandler.error("Can't set default language except on top-level Class: "+classEl);
        }
    }

    private String className(Element classEl) {
        DeclaredType dt = (DeclaredType) classEl.asType();
        TypeElement te = (TypeElement) dt.asElement();

        if ( ! allowedNesting(te.getNestingKind())) {
            messageHandler.error(classEl, "Unsupported nesting level");
        }
        return te.getQualifiedName().toString();
    }

    private boolean allowedNesting(NestingKind nk) {
        switch (nk) {
            case TOP_LEVEL: return true;
            case MEMBER:    return true;
            case LOCAL:     return false;
            case ANONYMOUS: return false;
            default:        return false;
        }
    }

    public EgLanguage languageForClassName(String className, EgLanguage defaultLanguage) {
        return languageForClassName.getOrDefault(className, defaultLanguage);
    }

    public Map<String, List<EgItem<?>>> getItemsByClassName() {
        // The inner lists are mutable, but this at least discourages modification
        return Collections.unmodifiableMap(itemsByClassName);
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
}
