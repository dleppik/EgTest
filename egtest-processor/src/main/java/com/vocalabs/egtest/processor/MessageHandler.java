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

    /** This will be removed once we support all the cases promised in README.md. */
    @Deprecated
    public void notYetSupported(Element el, Annotation a) { notYetSupported(el, a.toString()); }

    /** This will be removed once we support all the cases promised in README.md. */
    @Deprecated
    public void notYetSupported(Element el, Annotation a, String description) { notYetSupported(el, description+" "+a); }

    /** This will be removed once we support all the cases promised in README.md. */
    @Deprecated
    public void notYetSupported(Element el, String description) {
        messager.printMessage(WARNING, "EgTest does not yet support "+description + " on "+elStr(el));
    }

    public void unsupported(Element el, Annotation a, String description) { unsupported(el, description+" "+a);}

    /** This includes all cases not promised in README.md or other documentation. */
    public void unsupported(Element el, String description) {
        Kind kind = (failOnUnsupported) ? ERROR : WARNING;
        messager.printMessage(kind, "EgTest does not support " + description + " on " + elStr(el));
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
