package com.vocalabs.egtest.processor.junit;

import com.squareup.javapoet.*;
import com.vocalabs.egtest.processor.AnnotationCollector;
import com.vocalabs.egtest.processor.EgTestWriter;
import com.vocalabs.egtest.processor.MessageHandler;
import com.vocalabs.egtest.processor.data.AnnotationData;
import com.vocalabs.egtest.processor.data.EgMatchesPatternData;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Build JUnit test classes
 */
public class JUnitClassWriter implements EgTestWriter {

    private final AnnotationSpec testAnnotation = AnnotationSpec.builder(ClassName.bestGuess("org.junit.Test")).build();

    private final File directoryToFill;
    private final boolean clearDirectoryOnWrite;

    public JUnitClassWriter(File directoryToFill, boolean clearDirectoryOnWrite) {
        this.directoryToFill = directoryToFill;
        this.clearDirectoryOnWrite = clearDirectoryOnWrite;
    }

    @Override
    public void write(AnnotationCollector annotationCollector) throws Exception {
        boolean didCreateDir = directoryToFill.mkdirs();
        if (clearDirectoryOnWrite  &&  ! didCreateDir) {
            File[] files = directoryToFill.listFiles();
            if (files == null)
                throw new IOException("Location for writing EgTest source is not a directory: "+directoryToFill);
            for (File f: files) {
                deleteRecursive(f);
            }
        }

        Map<String, List<AnnotationData<?>>> itemsByClassName = annotationCollector.getItemsByClassName();
        for (Map.Entry<String, List<AnnotationData<?>>> entry: itemsByClassName.entrySet()) {
            createFile(directoryToFill, entry.getValue(), annotationCollector.getMessageHandler());
        }
    }

    private void createFile(File dir, List<AnnotationData<?>> items, MessageHandler messageHandler) throws Exception {
        TypeElement classElement = JavaModelUtil.topLevelClass(items.get(0).element());

        String className = classElement.getSimpleName() +"$EgTest";


        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
                //.addMethod(createEgMatchPatternTest(Arrays.asList("dleppik@vocalabs.com", "dleppik@vocalabs.example.com")));

        addEgMatchPatternTests(items, typeSpecBuilder);


        TypeSpec helloWorld = typeSpecBuilder.build();


        PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
        if (packageElement == null) {
            throw new IllegalArgumentException("EgTest does not support classes without packages. ("+classElement+")");
        }

        JavaFile javaFile = JavaFile.builder(packageElement.getQualifiedName().toString(), helloWorld)
                .build();

        messageHandler.note("Writing to "+dir);
        javaFile.writeTo(dir);
        javaFile.writeTo(System.out); // XXX
    }

    private void addEgMatchPatternTests(List<AnnotationData<?>> examples, TypeSpec.Builder toAddTo) {
        final Map<Element, List<EgMatchesPatternData>> byElement = examples.stream()
                .filter(it -> it instanceof EgMatchesPatternData)
                .map(it -> (EgMatchesPatternData) it)
                .collect(Collectors.groupingBy(EgMatchesPatternData::element));

        for (Map.Entry<Element, List<EgMatchesPatternData>> entry: byElement.entrySet()) {
            Element element = entry.getKey();
            String methodName = "testMatch"+element.getSimpleName();
            MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(testAnnotation)
                    .returns(void.class);

            for (EgMatchesPatternData example: entry.getValue()) {
                String s = example.toMatch();
                ClassName assertTrue = ClassName.get("org.junit.Assert", "assertTrue");
                ClassName className = ClassName.get((TypeElement) element.getEnclosingElement());
                String patternName = element.getSimpleName().toString();
                specBuilder.addCode(
                        "$T($S, $T.$L.matcher($S).matches();\n",
                        assertTrue, s, className, patternName, s);
            }

            toAddTo.addMethod(specBuilder.build());
        }
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
