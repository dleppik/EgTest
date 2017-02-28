package com.vocalabs.egtest.processor.junit;

import com.squareup.javapoet.*;
import com.vocalabs.egtest.annotation.EgMatches;
import com.vocalabs.egtest.annotation.EgNoMatch;
import com.vocalabs.egtest.processor.AnnotationCollector;
import com.vocalabs.egtest.processor.EgTestWriter;
import com.vocalabs.egtest.processor.MessageHandler;
import com.vocalabs.egtest.processor.data.Example;
import com.vocalabs.egtest.processor.data.FunctionMatchExample;
import com.vocalabs.egtest.processor.data.PatternMatchExample;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Build JUnit test source code
 */
public class JUnitWriter implements EgTestWriter {

    private final AnnotationSpec testAnnotation = AnnotationSpec.builder(ClassName.bestGuess("org.junit.Test")).build();

    private final File directoryToFill;
    private final AlreadyExistsBehavior directoryExistsBehavior;

    public JUnitWriter(File directoryToFill, AlreadyExistsBehavior directoryExistsBehavior) {
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

        MessageHandler messageHandler = annotationCollector.getMessageHandler();
        Map<String, List<Example<?>>> itemsByClassName = annotationCollector.getItemsByClassName();
        for (Map.Entry<String, List<Example<?>>> entry: itemsByClassName.entrySet()) {
            JUnitClassWriter.createFileSpec(entry.getKey(), messageHandler, entry.getValue())
                .writeTo(directoryToFill);
        }
    }

    private void createFile(File dir, List<Example<?>> items, MessageHandler messageHandler) throws Exception {
        TypeElement classElement = JavaModelUtil.topLevelClass(items.get(0).element());

        String className = classElement.getSimpleName() +"$EgTest";


        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        addPatternMatchTests(items, typeSpecBuilder, messageHandler);
        addFunctionMatchTests(items, typeSpecBuilder, messageHandler);

        TypeSpec javaFileSpec = typeSpecBuilder.build();

        PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
        if (packageElement == null) {
            throw new IllegalArgumentException("EgTest does not support classes without packages. ("+classElement+")");
        }

        JavaFile javaFile = JavaFile.builder(packageElement.getQualifiedName().toString(), javaFileSpec)
                .build();

        javaFile.writeTo(dir);
    }

    private void addPatternMatchTests(List<Example<?>> examples, TypeSpec.Builder toAddTo, MessageHandler messageHandler) {
        final Map<Element, List<PatternMatchExample>> byElement = examples.stream()
                .filter(it -> it instanceof PatternMatchExample)
                .map(it -> (PatternMatchExample) it)
                .collect(Collectors.groupingBy(PatternMatchExample::element));
        addPatternMatchTests(toAddTo, byElement, messageHandler);
    }

    private void addPatternMatchTests(TypeSpec.Builder toAddTo, Map<Element, List<PatternMatchExample>> byElement, MessageHandler messageHandler) {
        for (Map.Entry<Element, List<PatternMatchExample>> entry: byElement.entrySet()) {
            Element element = entry.getKey();
            String methodName = "testMatch"+element.getSimpleName();

            MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(testAnnotation)
                    .returns(void.class);

            for (PatternMatchExample example: entry.getValue()) {
                if ( ! element.getModifiers().contains(Modifier.STATIC)) {
                    messageHandler.notYetSupported(element, example.annotation(), "non-public"); // TODO
                    continue; // TODO
                }
                if (! visible(element) ) {
                    messageHandler.notYetSupported(element, example.annotation(), "non-visible"); // TODO
                    continue; // TODO
                }

                ClassName assertion = booleanAssertion(example.annotation());
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

    private void addFunctionMatchTests(List<Example<?>> examples, TypeSpec.Builder toAddTo, MessageHandler messageHandler) {
        final Map<Element, List<FunctionMatchExample>> byElement = examples.stream()
                .filter(it -> it instanceof FunctionMatchExample)
                .map(it -> (FunctionMatchExample) it)
                .collect(Collectors.groupingBy(FunctionMatchExample::element));
        addFunctionMatchTests(toAddTo, byElement, messageHandler);
    }

    private void addFunctionMatchTests(TypeSpec.Builder toAddTo, Map<Element, List<FunctionMatchExample>> byElement, MessageHandler messageHandler) {
        for (Map.Entry<Element, List<FunctionMatchExample>> entry: byElement.entrySet()) {
            Element element = entry.getKey();
            String methodName = "testMatch"+element.getSimpleName();
            MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(testAnnotation)
                    .returns(void.class);

            for (FunctionMatchExample example: entry.getValue()) {
                if ( ! element.getModifiers().contains(Modifier.STATIC)) {
                    messageHandler.notYetSupported(element, example.annotation(), "non-public"); // TODO
                    continue; // TODO
                }
                if (! visible(element) ) {
                    messageHandler.notYetSupported(element, example.annotation(), "non-visible"); // TODO
                    continue; // TODO
                }

                ClassName assertion = booleanAssertion(example.annotation());
                String description = example.annotation().annotationType().getSimpleName()+" "+example.toMatch();

                ClassName className = ClassName.get((TypeElement) element.getEnclosingElement());
                String patternName = element.getSimpleName().toString();
                specBuilder.addCode(
                        "$L($S, $T.$L($S));\n",
                        assertion, description, className, patternName, example.toMatch());
            }

            toAddTo.addMethod(specBuilder.build());
        }
    }

    private boolean visible(Element el) {
        Set<Modifier> modifiers = el.getModifiers();
        return modifiers.contains(Modifier.PUBLIC) || modifiers.contains(Modifier.DEFAULT);
    }

    private ClassName booleanAssertion(Annotation annotation) {
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
