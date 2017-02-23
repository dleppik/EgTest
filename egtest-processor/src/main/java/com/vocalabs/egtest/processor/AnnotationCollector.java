package com.vocalabs.egtest.processor;


import com.vocalabs.egtest.processor.data.AnnotationData;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.*;
import java.util.stream.Stream;

public class AnnotationCollector {
    private final Map<String, List<AnnotationData<?>>> itemsByClassName = new TreeMap<>();
    private final MessageHandler messageHandler;

    public AnnotationCollector(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void add(AnnotationData<?> data) {
        Element classEl = data.element().getEnclosingElement();
        if (classEl.getKind().equals(ElementKind.CLASS)) {
            String name = className(classEl);
            messageHandler.note("Got class "+name);
            itemsByClassName.computeIfAbsent(name, x -> new ArrayList<>())
                    .add(data);
        }
        else {
            messageHandler.error("Container is not a class: "+data.element());
        }
    }

    private String className(Element classEl) {
        DeclaredType dt = (DeclaredType) classEl.asType();
        TypeElement te = (TypeElement) dt.asElement();

        if ( ! allowedNesting(te.getNestingKind())) {
            messageHandler.error("Unsupported nesting level");
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

    public Map<String, List<AnnotationData<?>>> getItemsByClassName() {
        // The inner lists are mutable, but this at least discourages modification
        return Collections.unmodifiableMap(itemsByClassName);
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
}
