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
    private final EgLanguage defaultLanguage;
    private final String packageName;

    /**
     * @param packageName The name of the enclosing package; these are imported into the script's namespace.
     */
    public CodeInjector(EgLanguage defaultLanguage, String packageName) {
        this.defaultLanguage = defaultLanguage;
        this.packageName = packageName;
        if (EgLanguage.INHERIT.equals(defaultLanguage)) {
            throw new IllegalArgumentException("Cannot use INHERIT as a default EgTest language");
        }
    }

    private LanguageInjector createInjector(EgLanguage language) {
        switch (language) {
            case    JAVA: return new JavaInjector();
            case  GROOVY: return new GroovyInjector(packageName);
            case INHERIT: return createInjector(defaultLanguage);
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
