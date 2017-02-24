package com.vocalabs.egtest.processor;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;

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
        // FIXME  removed for clarity
        // note("Not yet supported: " + annotation + " on " + el.getSimpleName());
    }

    public void unsupported(Annotation annotation, Element el) {
        Diagnostic.Kind kind = (failOnUnsupported) ? Diagnostic.Kind.ERROR : Diagnostic.Kind.NOTE;
        messager.printMessage(kind, "Not supported: " + annotation + " on " + el.getSimpleName());
    }

    public void note(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, message);
    }

    /** Print the stack trace as well as the message */
    public void error(Exception error) {
        error.printStackTrace();
        messager.printMessage(Diagnostic.Kind.ERROR, error.getMessage());
    }

    public void error(String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message);
    }
}
