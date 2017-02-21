package com.vocalabs.egtest.processor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import com.vocalabs.egtest.annotation.*;

import java.lang.annotation.Annotation;
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
        "com.vocalabs.egtest.annotation.EgNoMatchContainer"})
public class EgAnnotationProcessor extends AbstractProcessor {

    private Messager messager = null;
    private Types typeUtils = null;
    private Elements elementUtils = null;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        note("ExampleAnnotationProcessor Starting up\noptions:\n"+processingEnv.getOptions());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        note("ExampleAnnotationProcessor got annotations "+annotations);

        Stream.of(
                Eg.class,
                EgContainer.class,
                EgException.class,
                EgExceptionContainer.class,
                EgMatches.class,
                EgMatchesContainer.class,
                EgNoMatch.class,
                EgNoMatchContainer.class)
                .forEach(cl -> {
            for (Element element: roundEnv.getElementsAnnotatedWith(cl)) {
                note("Annotation of type "+cl+" for "+element+" is "+element.getAnnotation(cl));
            }
        });
        return true;
    }

    private void note(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, message);
    }
}
