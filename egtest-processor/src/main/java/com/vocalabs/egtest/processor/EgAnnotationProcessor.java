package com.vocalabs.egtest.processor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;

import com.vocalabs.egtest.annotation.*;
import com.vocalabs.egtest.processor.data.EgMatchesPatternData;
import com.vocalabs.egtest.processor.junit.JUnitClassWriter;
import com.vocalabs.egtest.processor.selftest.EgSelfTest;

import java.io.File;
import java.util.Set;
import java.util.stream.Stream;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "com.vocalabs.egtest.annotation.Eg",
        "com.vocalabs.egtest.annotation.EgContainer",
        "com.vocalabs.egtest.annotation.EgException",
        "com.vocalabs.egtest.annotation.EgExceptionContainer",
        "com.vocalabs.egtest.annotation.EgMatches",
        "com.vocalabs.egtest.annotation.EgMatchesContainer",
        "com.vocalabs.egtest.annotation.EgNoMatch",
        "com.vocalabs.egtest.annotation.EgNoMatchContainer",
        "com.vocalabs.egtest.processor.selftest.EgSelfTest"})
@SupportedOptions("egtest.targetDirectory")
public class EgAnnotationProcessor extends AbstractProcessor {

    private MessageHandler messageHandler = null;
    private boolean firstPass = true;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messageHandler = new MessageHandler(processingEnv.getMessager(), false);
        firstPass = true;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            AnnotationCollector collector = new AnnotationCollector(messageHandler);

            for (Element el: roundEnv.getElementsAnnotatedWith(EgMatches.class)) {
                handleMatch(el.getAnnotation(EgMatches.class), el, collector);
            }

            for (Element el: roundEnv.getElementsAnnotatedWith(EgMatchesContainer.class)) {
                EgMatchesContainer egc = el.getAnnotation(EgMatchesContainer.class);
                for (EgMatches egMatches: egc.value()) {
                    handleMatch(egMatches, el, collector);
                }
            }

            File targetDir = new File(processingEnv.getOptions().get("egtest.targetDirectory"));
            EgTestWriter.AlreadyExistsBehavior onExists = (firstPass)
                    ? EgTestWriter.AlreadyExistsBehavior.DELETE
                    : EgTestWriter.AlreadyExistsBehavior.OVERWRITE;
            new JUnitClassWriter(targetDir, onExists).write(collector);

            // Unsupported as of yet
            Stream.of(
                    Eg.class,
                    EgContainer.class,
                    EgException.class,
                    EgExceptionContainer.class,
                    EgNoMatch.class,
                    EgNoMatchContainer.class,
                    EgSelfTest.class)
                    .forEach(cl -> {
                        for (Element element: roundEnv.getElementsAnnotatedWith(cl)) {
                            messageHandler.notYetSupported(element.getAnnotation(cl), element);
                        }
                    });
        }
        catch (Exception ex) {
            messageHandler.error(ex);
        }
        firstPass = false;
        return true;
    }

    private void handleMatch(EgMatches egMatches, Element element, AnnotationCollector collector) throws Exception {
        if (isPattern(element)) {
            VariableElement el = (VariableElement) element;
            if (el.getModifiers().contains(Modifier.STATIC) && visible(el))
                    collector.add(new EgMatchesPatternData(egMatches, el));
            else
                messageHandler.notYetSupported(egMatches, el); // TODO
        }
        if (element instanceof ExecutableElement) {
            messageHandler.notYetSupported(egMatches, element); // TODO
        }
        else {
            messageHandler.unsupported(egMatches, element); // TODO
        }
    }

    private boolean isPattern(Element element) {
        return element instanceof VariableElement
            && element.getKind().equals(ElementKind.FIELD)
            && element.asType().toString().equals("java.util.regex.Pattern");
    }


    private boolean visible(Element el) {
        Set<Modifier> modifiers = el.getModifiers();
        return modifiers.contains(Modifier.PUBLIC) || modifiers.contains(Modifier.DEFAULT);
    }


}
