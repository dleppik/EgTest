package com.vocalabs.egtest.processor.junit;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.vocalabs.egtest.annotation.EgLanguage;

import javax.lang.model.type.TypeMirror;
import java.util.EnumMap;

/**
 * Container for {@link LanguageInjector}s which constructs them as needed; each {@link JUnitClassWriter} should
 * have exactly one of these.
 */
public class CodeInjector {
    private final String packageName;

    /**
     * @param packageName The name of the enclosing package; these are imported into the script's namespace.
     */
    public CodeInjector(String packageName) {
        this.packageName = packageName;
    }

    private LanguageInjector createInjector(EgLanguage language) {
        switch (language) {
            case JAVA:   return new JavaInjector();
            case GROOVY: return new GroovyInjector(packageName);
            default:     throw new IllegalArgumentException("Unexpected language "+language);
        }
    }

    private final EnumMap<EgLanguage, LanguageInjector> map = new EnumMap<>(EgLanguage.class);

    public LanguageInjector languageInjector(EgLanguage language) {
        return map.computeIfAbsent(language, this::createInjector);
    }

    public void add(EgLanguage language, MethodSpec.Builder specBuilder, TypeMirror type, String egText) {
        languageInjector(language).add(specBuilder, type, egText);
    }

    public void decorateClass(TypeSpec.Builder toDecorate) {
        for (LanguageInjector li : map.values()) {
            li.decorateClass(toDecorate);
        }
    }

    public String getPackageName() { return packageName; }
}
