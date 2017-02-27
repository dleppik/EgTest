package com.vocalabs.egtest.processor;

import com.vocalabs.egtest.processor.junit.JavaModelUtil;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

import static javax.tools.Diagnostic.Kind;
import static javax.tools.Diagnostic.Kind.*;

/**
 * Wraps a Messager to provide standardized messages and actions.
 */
public class MessageHandler {
    private final Messager messager;
    private final boolean failOnUnsupported;

    public MessageHandler(Messager messager, boolean failOnUnsupported) {
        this.messager = messager;
        this.failOnUnsupported = failOnUnsupported;
    }

    public void notYetSupported(Annotation annotation, Element el) {
        messager.printMessage(WARNING, "EgTest does not yet support " + annotation + " on "+elStr(el));
    }

    public void unsupported(Annotation annotation, Element el) {
        Kind kind = (failOnUnsupported) ? ERROR : WARNING;
        messager.printMessage(kind, "EgTest does not support " + annotation + " on " + elStr(el));
    }

    private String elStr(Element el) {
        return el+" in "+ JavaModelUtil.topLevelClass(el);
    }

    public void note(String message) {
        messager.printMessage(NOTE, message);
    }

    /** Print the stack trace as well as the message */
    public void error(Exception error) {
        error.printStackTrace();
        messager.printMessage(ERROR, error.getMessage());
    }

    public void error(String message) {
        messager.printMessage(ERROR, message);
    }
}
