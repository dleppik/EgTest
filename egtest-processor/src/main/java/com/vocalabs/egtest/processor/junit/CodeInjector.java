package com.vocalabs.egtest.processor.junit;

import com.squareup.javapoet.TypeSpec;
import com.vocalabs.egtest.annotation.EgLanguage;
import com.vocalabs.egtest.processor.AnnotationCollector;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.util.EnumMap;
import java.util.Map;

/**
 * Container for {@link LanguageInjector}s which constructs them as needed; each {@link ClassWriter} should
 * have exactly one of these.
 */
public class CodeInjector {
    private final Map<String,EgLanguage> languageForClassName;
    private final TypeElement classElement;
    private final EgLanguage defaultLanguage;
    private final String packageName;

    public CodeInjector(TypeElement classElement, Map<String,EgLanguage> languageForClassName, EgLanguage defaultLanguage) {
        this.classElement = classElement;
        this.languageForClassName = languageForClassName;
        this.defaultLanguage = languageForClassName.getOrDefault(AnnotationCollector.className(classElement), defaultLanguage);

        PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
        this.packageName = ""+packageElement.getQualifiedName();
        if (EgLanguage.INHERIT.equals(this.defaultLanguage)) {
            throw new IllegalArgumentException("Cannot use INHERIT as a default EgTest language");
        }
    }

    private LanguageInjector createInjector(EgLanguage language) {
        switch (language) {
            case    JAVA: return new JavaInjector();
            case  GROOVY: return new GroovyInjector(packageName);
            case INHERIT: throw new IllegalArgumentException("Bug: should have used the default language");
            default:     throw new IllegalArgumentException("Unexpected language "+language);
        }
    }

    private final EnumMap<EgLanguage, LanguageInjector> map = new EnumMap<>(EgLanguage.class);

    /**
     * Return an injector for this language. Each CodeInjector has at most one injector for each language.
     * @throws IllegalArgumentException for {@link EgLanguage#INHERIT}.
     */
    private LanguageInjector languageInjector(EgLanguage language) {
        return map.computeIfAbsent(language, this::createInjector);
    }

    /**
     * Calls {@link #languageInjector(EgLanguage)} for an annotation with the given annotated language (typically
     * INHERIT) and enclosing class.
     */
    public LanguageInjector languageInjector(EgLanguage annotatedLanguage, Element enclosingElement) {
        EgLanguage language = EgLanguage.INHERIT.equals(annotatedLanguage)
                ? classLanguage(enclosingElement)
                : annotatedLanguage;
        return  languageInjector(language);
    }

    private EgLanguage classLanguage(Element el) {
        if (el.equals(classElement))
            return defaultLanguage;  // Typical case
        EgLanguage lang = languageForClassName.get(AnnotationCollector.className(el));
        return (lang==null)
                ? classLanguage(el.getEnclosingElement()) // Java doesn't actually support tail recursion
                : lang;
    }

    public void decorateClass(TypeSpec.Builder toDecorate) {
        for (LanguageInjector li : map.values()) {
            li.decorateClass(toDecorate);
        }
    }

    public String getPackageName() { return packageName; }
}
