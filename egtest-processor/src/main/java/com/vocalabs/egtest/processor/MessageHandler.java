package com.vocalabs.egtest.processor;

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

    public void unsupported(Element el, Annotation a, String description) { unsupported(el, description+" "+a);}

    /**
     * This includes all cases not promised in README.md or other documentation.
     * @param el the element with the unsupported annotation(s)
     * @param description the kind of thing that's unsupported, e.g. "inner classes"
     */
    public void unsupported(Element el, String description) {
        Kind kind = (failOnUnsupported) ? ERROR : WARNING;
        messager.printMessage(kind, "EgTest does not support " + description + " on " + elStr(el), el);
    }

    private String elStr(Element el) {
        return el+" in "+ JavaModelUtil.topLevelClass(el);
    }

    public void note(String message) {
        messager.printMessage(NOTE, message);
    }

    /**
     * Print the stack trace as well as the message.
     * @param error a caught exception
     */
    public void error(Exception error) {
        error.printStackTrace();
        messager.printMessage(ERROR, error.getMessage());
    }

    public void error(String message) {
        messager.printMessage(ERROR, message);
    }

    public void error(Element el, String message) {
        messager.printMessage(ERROR, message, el);
    }
}
