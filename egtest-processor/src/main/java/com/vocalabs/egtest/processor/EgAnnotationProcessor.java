package com.vocalabs.egtest.processor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

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
public class EgAnnotationProcessor extends AbstractProcessor {

    private Types typeUtils = null;
    private Elements elementUtils = null;
    private MessageHandler messageHandler = null;

    private boolean failOnUnsupported = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messageHandler = new MessageHandler(processingEnv.getMessager(), false);

        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
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

            new JUnitClassWriter(new File("/tmp/junit-items"), true).write(collector);

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
        return true;
    }

    private void handleMatch(EgMatches egMatches, Element el, AnnotationCollector collector) throws Exception {
        if (el.getKind().equals(ElementKind.FIELD)) {
            messageHandler.note("###### Got field ("+el.getSimpleName()+") for "+egMatches);
            if (el.getModifiers().contains(Modifier.STATIC)) {
                if (visible(el)) {
                    collector.add(new EgMatchesPatternData(egMatches, el)); // TODO confirm it's a Pattern
                    // TODO
                }
                else
                    messageHandler.notYetSupported(egMatches, el);
            }
            else {
                messageHandler.notYetSupported(egMatches, el); // TODO
            }
        }
        else {
            messageHandler.unsupported(egMatches, el); // TODO
        }
    }


    private boolean visible(Element el) {
        Set<Modifier> modifiers = el.getModifiers();
        return modifiers.contains(Modifier.PUBLIC) || modifiers.contains(Modifier.DEFAULT);
    }


}
