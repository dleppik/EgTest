package com.vocalabs.egtest.processor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;

import java.util.*;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "com.vocalabs.egtest.annotation.Eg",
        "com.vocalabs.egtest.annotation.EgContainer",
        "com.vocalabs.egtest.annotation.EgDefaultLanguage",
        "com.vocalabs.egtest.annotation.EgException",
        "com.vocalabs.egtest.annotation.EgExceptionContainer",
        "com.vocalabs.egtest.annotation.EgMatch",
        "com.vocalabs.egtest.annotation.EgMatchContainer",
        "com.vocalabs.egtest.annotation.EgNoMatch",
        "com.vocalabs.egtest.annotation.EgNoMatchContainer",
        "com.vocalabs.egtest.processor.selftest.EgSelfTest"})
@SupportedOptions({
        Settings.TARGET_DIR_KEY,
        Settings.DIR_EXISTS_BEHAVIOR_KEY,
        Settings.FAIL_ON_UNSUPPORTED_KEY,
        Settings.SELF_TEST_KEY,
        Settings.TARGET_LANGUAGE_KEY})
public class EgAnnotationProcessor extends AbstractProcessor {
    private Messager messager = null;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        AnnotationAnalyzer.findExamples(roundEnv, messager);
        return true;
    }
}
