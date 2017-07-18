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
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * Build JUnit test classes; used by {@link JUnitMainWriter}.
 */
class JUnitClassWriter {

    static JavaFile createFileSpec(EgLanguage defaultLanguage,
                                   String classUnderTestName,
                                   MessageHandler messageHandler,
                                   List<EgItem<?>> items)
    throws Exception {
        return new JUnitClassWriter(defaultLanguage, classUnderTestName, messageHandler, items)
                .createFileSpec();
    }

    private final MessageHandler messageHandler;
    private final List<EgItem<?>> items;
    private final TypeElement classElement;
    private final String className;
    private final CodeInjector codeInjector;


    private JUnitClassWriter(EgLanguage defaultLanguage, String name, MessageHandler messageHandler, List<EgItem<?>> items) {
        this.messageHandler = messageHandler;
        this.items = items;
        if (items.isEmpty())
            throw new IllegalArgumentException("Must have at least one Example to write a test file for "+name);
        this.classElement = JavaModelUtil.topLevelClass(items.get(0).getElement());
        this.className = classElement.getSimpleName() +"$EgTest";

        PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
        if (packageElement == null || packageElement.getQualifiedName() == null) {
            messageHandler.unsupported(classElement, "EgTest does not support classes without packages.");
            this.codeInjector = new CodeInjector(defaultLanguage, "");
        }
        else {
            String packageName = ""+packageElement.getQualifiedName();
            this.codeInjector = new CodeInjector(defaultLanguage, packageName);
        }
    }

    private JavaFile createFileSpec() throws Exception {
        AnnotationSpec generated = AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", "com.vocalabs.egtest.EgTest")
                .build();
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotation(generated);
        JUnitExampleWriter.write(this, typeSpecBuilder);
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
