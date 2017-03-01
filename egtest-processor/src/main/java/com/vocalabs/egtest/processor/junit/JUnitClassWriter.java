package com.vocalabs.egtest.processor.junit;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.vocalabs.egtest.processor.MessageHandler;
import com.vocalabs.egtest.processor.data.Example;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * Build JUnit test classes; used by {@link JUnitWriter}.
 */
class JUnitClassWriter {

    static JavaFile createFileSpec(String classUnderTestName,
                                   MessageHandler messageHandler,
                                   List<Example<?>> items)
    throws Exception {
        return new JUnitClassWriter(classUnderTestName, messageHandler, items)
                .createFileSpec();
    }

    private final MessageHandler messageHandler;
    private final List<Example<?>> items;
    private final TypeElement classElement;
    private final String className;


    private JUnitClassWriter(String name, MessageHandler messageHandler, List<Example<?>> items) {
        this.messageHandler = messageHandler;
        this.items = items;
        if (items.isEmpty())
            throw new IllegalArgumentException("Must have at least one Example to write a test file for "+name);
        this.classElement = JavaModelUtil.topLevelClass(items.get(0).getElement());
        this.className = classElement.getSimpleName() +"$EgTest";
    }

    private JavaFile createFileSpec() throws Exception {
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        JUnitExampleWriter.write(this, typeSpecBuilder);
        TypeSpec javaFileSpec = typeSpecBuilder.build();

        PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
        if (packageElement == null) {
            messageHandler.unsupported(classElement, "EgTest does not support classes without packages.");
        }
        String packageName = packageElement.getQualifiedName().toString();
        return JavaFile.builder(packageName, javaFileSpec).build();
    }

    MessageHandler getMessageHandler() { return messageHandler; }

    List<Example<?>> getItems() { return items; }

    TypeElement getClassElement() { return classElement; }

}
