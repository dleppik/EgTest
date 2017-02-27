package com.vocalabs.egtest.processor.junit;

import com.squareup.javapoet.*;
import com.vocalabs.egtest.annotation.EgMatches;
import com.vocalabs.egtest.annotation.EgNoMatch;
import com.vocalabs.egtest.processor.AnnotationCollector;
import com.vocalabs.egtest.processor.EgTestWriter;
import com.vocalabs.egtest.processor.MessageHandler;
import com.vocalabs.egtest.processor.data.Example;
import com.vocalabs.egtest.processor.data.PatternMatchExample;

import javax.lang.model.element.*;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Build JUnit test classes
 */
public class JUnitClassWriter implements EgTestWriter {

    private final AnnotationSpec testAnnotation = AnnotationSpec.builder(ClassName.bestGuess("org.junit.Test")).build();

    private final File directoryToFill;
    private final AlreadyExistsBehavior directoryExistsBehavior;

    public JUnitClassWriter(File directoryToFill, AlreadyExistsBehavior directoryExistsBehavior) {
        this.directoryToFill = directoryToFill;
        this.directoryExistsBehavior = directoryExistsBehavior;
    }

    @Override
    public void write(AnnotationCollector annotationCollector) throws Exception {
        if (AlreadyExistsBehavior.FAIL.equals(directoryExistsBehavior) && directoryToFill.exists()) {
            annotationCollector.getMessageHandler()
                    .error("EgTest target directory exists ("+directoryToFill.getAbsolutePath()+")");
            return;
        }

        boolean didCreateDir = directoryToFill.mkdirs();
        if (AlreadyExistsBehavior.DELETE.equals(directoryExistsBehavior) &&  ! didCreateDir) {
            File[] files = directoryToFill.listFiles();
            if (files == null)
                throw new IOException("Location for writing EgTest source is not a directory: "+directoryToFill);
            for (File f: files) {
                deleteRecursive(f);
            }
        }

        Map<String, List<Example<?>>> itemsByClassName = annotationCollector.getItemsByClassName();
        for (Map.Entry<String, List<Example<?>>> entry: itemsByClassName.entrySet()) {
            createFile(directoryToFill, entry.getValue(), annotationCollector.getMessageHandler());
        }
    }

    private void createFile(File dir, List<Example<?>> items, MessageHandler messageHandler) throws Exception {
        TypeElement classElement = JavaModelUtil.topLevelClass(items.get(0).element());

        String className = classElement.getSimpleName() +"$EgTest";


        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        addEgMatchPatternTests(items, typeSpecBuilder);

        TypeSpec helloWorld = typeSpecBuilder.build();

        PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
        if (packageElement == null) {
            throw new IllegalArgumentException("EgTest does not support classes without packages. ("+classElement+")");
        }

        JavaFile javaFile = JavaFile.builder(packageElement.getQualifiedName().toString(), helloWorld)
                .build();

        javaFile.writeTo(dir);
    }

    private void addEgMatchPatternTests(List<Example<?>> examples, TypeSpec.Builder toAddTo) {
        final Map<Element, List<PatternMatchExample>> byElement = examples.stream()
                .filter(it -> it instanceof PatternMatchExample)
                .map(it -> (PatternMatchExample) it)
                .collect(Collectors.groupingBy(PatternMatchExample::element));

        for (Map.Entry<Element, List<PatternMatchExample>> entry: byElement.entrySet()) {
            Element element = entry.getKey();
            String methodName = "testMatch"+element.getSimpleName();
            MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(testAnnotation)
                    .returns(void.class);

            for (PatternMatchExample example: entry.getValue()) {
                ClassName assertion = assertion(example.annotation());
                String description = example.annotation().annotationType().getSimpleName()+" "+example.toMatch();

                ClassName className = ClassName.get((TypeElement) element.getEnclosingElement());
                String patternName = element.getSimpleName().toString();
                specBuilder.addCode(
                        "$L($S, $T.$L.matcher($S).matches());\n",
                        assertion, description, className, patternName, example.toMatch());
            }

            toAddTo.addMethod(specBuilder.build());
        }
    }

    private ClassName assertion(Annotation annotation) {
        final String boolStr;
        if (annotation instanceof EgMatches)
            boolStr = "True";
        else if (annotation instanceof EgNoMatch)
            boolStr = "False";
        else throw new IllegalArgumentException(this+" doesn't handle "+annotation);
        return ClassName.get("org.junit.Assert", "assert"+boolStr);
    }

    private void deleteRecursive(File file) throws Exception {
        File[] files = file.listFiles();
        if (files != null) {
            for (File f: files) {
                deleteRecursive(f);
            }
        }
        file.delete();
    }
}
