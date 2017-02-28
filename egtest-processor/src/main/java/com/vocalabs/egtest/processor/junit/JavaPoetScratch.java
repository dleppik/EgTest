package com.vocalabs.egtest.processor.junit;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

/**
 * Delete before March 2017.
 */
@Deprecated
public class JavaPoetScratch {
    public static void main(String[] args) throws Exception {

        ClassName name = ClassName.get("com.vocalabs", "Greeter");

        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .build();

        MethodSpec greeter = MethodSpec.methodBuilder("greet")
                .addParameter(String.class, "target")
                .returns(void.class)
                .addStatement("$T.out.println($S + $N)", System.class, "Hello, ", "target")
                .build();

        MethodSpec factory = MethodSpec.methodBuilder("instance")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addStatement("return new $T()", name)
                .returns(name)
                .build();

        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T greeter = new $T()", name, name)
                .addStatement("greeter.$N($S)", greeter, "JavaPoet")
                .addStatement("$N().$N($S)", factory, greeter, "JavaPoet")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();


        TypeSpec helloWorld = TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .addMethod(factory)
                .addMethod(constructor)
                .addMethod(greeter)
                .build();

        JavaFile javaFile = JavaFile.builder(name.packageName(), helloWorld)
                .build();

        javaFile.writeTo(System.out);
    }
}
