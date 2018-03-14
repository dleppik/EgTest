package com.vocalabs.egtest.processor.junit;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.vocalabs.egtest.annotation.EgLanguage;
import com.vocalabs.egtest.processor.JavaModelUtil;
import com.vocalabs.egtest.processor.MessageHandler;
import com.vocalabs.egtest.processor.data.EgItem;

import javax.annotation.Generated;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Map;

/**
 * Build JUnit test classes; used by {@link AllClassesWriter}.
 */
public class ClassWriter {

    static JavaFile createFileSpec(Map<String,EgLanguage> languageForClassName,
                                   EgLanguage defaultLanguage,
                                   String classUnderTestName,
                                   MessageHandler messageHandler,
                                   List<EgItem<?>> items)
    {
        if (items.isEmpty())
            throw new IllegalArgumentException("Must have at least one Example to write a test file for "+classUnderTestName);
        TypeElement classElement = JavaModelUtil.topLevelClass(items.get(0).getElement());
        CodeInjector codeInjector = new CodeInjector(classElement, languageForClassName, defaultLanguage);
        return new ClassWriter(codeInjector, classElement, messageHandler, items)
                .createFileSpec();
    }

    private final MessageHandler messageHandler;
    private final List<EgItem<?>> items;
    private final TypeElement classElement;
    private final String className;
    private final CodeInjector codeInjector;


    private ClassWriter(CodeInjector codeInjector, TypeElement classElement, MessageHandler messageHandler, List<EgItem<?>> items) {
        this.codeInjector = codeInjector;
        this.messageHandler = messageHandler;
        this.items = items;
        this.classElement = JavaModelUtil.topLevelClass(items.get(0).getElement());
        this.className = classElement.getSimpleName() +"$EgTest";
    }

    private JavaFile createFileSpec() {
        AnnotationSpec generated = AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", "com.vocalabs.egtest.EgTest")
                .build();
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(generated);
        TestWriter.write(this, typeSpecBuilder);
        codeInjector.decorateClass(typeSpecBuilder);
        TypeSpec javaFileSpec = typeSpecBuilder.build();
        JavaFile.Builder fileBuilder = JavaFile.builder(codeInjector.getPackageName(), javaFileSpec);
        return fileBuilder.build();
    }

    MessageHandler getMessageHandler() { return messageHandler; }

    List<EgItem<?>> getItems() { return items; }

    TypeElement getClassElement() { return classElement; }

    public CodeInjector getCodeInjector() { return codeInjector; }
}
