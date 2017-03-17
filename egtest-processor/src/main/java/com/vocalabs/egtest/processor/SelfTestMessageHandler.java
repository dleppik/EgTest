package com.vocalabs.egtest.processor;

import com.vocalabs.egtest.processor.selftest.EgSelfTest;
import com.vocalabs.egtest.processor.selftest.ExpectedBehavior;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import java.io.*;
import java.lang.annotation.Annotation;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.*;

/** Captures errors which are expected for the self-test. */
public class SelfTestMessageHandler extends MessageHandler {

    private final Map<String,String> unsupportedElements = new HashMap<>();
    private final Map<String,String> errorElements = new HashMap<>();

    public SelfTestMessageHandler(Messager messager) {
        super(messager, false);
    }

    @Override
    public void unsupported(Element el, Annotation a, String description) {
        if ( ! addIfUnsupported(el, description))
            super.unsupported(el, a, description);
    }

    @Override
    public void unsupported(Element el, String description) {
        if ( ! addIfUnsupported(el, description))
            super.unsupported(el, description);
    }

    @Override
    public void error(Element el, String message) {
        if ( ! addIfExpected(ExpectedBehavior.BUILD_FAILS, el, message, errorElements) )
            super.error(el, message);
    }

    private boolean addIfUnsupported(Element el, String description) {
        return addIfExpected(ExpectedBehavior.UNSUPPORTED_CASE, el, description, unsupportedElements);
    }

    private boolean addIfExpected(ExpectedBehavior expected, Element el, String message, Map<String, String> map) {
        EgSelfTest selfTest = el.getAnnotation(EgSelfTest.class);
        if (selfTest != null  &&  expected.equals(selfTest.value())) {
            map.put(JavaModelUtil.key(el), message);
            return true;
        }
        return false;
    }

    /** Write the unsupported/error messages with the unit tests so they can be read. */
    public void write(Path dir) throws IOException {
        write(dir.resolve("unsupportedElements.txt"), unsupportedElements);
        write(dir.resolve("errorElements.txt"), errorElements);
    }

    private void write(Path path, Map<String,String> map) throws IOException {
        String lines = map.entrySet().stream()
                .map(en -> en.getKey()+"\t"+en.getValue()+"\n")
                .collect(Collectors.joining());
        if (! Files.exists(path.getParent()))
            Files.createDirectories(path.getParent());
        Files.write(path, lines.getBytes("UTF-8"), CREATE, WRITE, APPEND);
    }
}
