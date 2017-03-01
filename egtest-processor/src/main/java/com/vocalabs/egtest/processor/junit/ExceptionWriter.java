package com.vocalabs.egtest.processor.junit;

import com.squareup.javapoet.TypeSpec;
import com.vocalabs.egtest.processor.data.ExceptionExample;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

public class ExceptionWriter extends JUnitExampleWriter<ExecutableElement, ExceptionExample> {
    public ExceptionWriter(ExecutableElement element, List<ExceptionExample> examples, JUnitClassWriter classWriter, TypeSpec.Builder toAddTo) {
        super(element, examples, classWriter, toAddTo);
    }

    @Override
    public void addTests() {
        if ( ! checkSupport())
            return;
        this.messageHandler.notYetSupported(element, "EgException");
    }
}
