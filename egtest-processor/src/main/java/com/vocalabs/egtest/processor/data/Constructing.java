package com.vocalabs.egtest.processor.data;

import com.vocalabs.egtest.annotation.Eg;
import com.vocalabs.egtest.annotation.EgException;
import com.vocalabs.egtest.annotation.EgMatch;
import com.vocalabs.egtest.annotation.EgNoMatch;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface Constructing<T extends Annotation> extends EgItem<T> {
    @NotNull
    default List<String> constructorArgs() {
        String[] a = constructorArgArray();
        return a == null ? Collections.emptyList() : Arrays.asList(a);
    }

    default String[] constructorArgArray() {
        Annotation a = getAnnotation();
        if (a instanceof Eg         ) return ((Eg)          a).construct();
        if (a instanceof EgMatch    ) return ((EgMatch)     a).construct();
        if (a instanceof EgNoMatch  ) return ((EgNoMatch)   a).construct();
        if (a instanceof EgException) return ((EgException) a).construct();
        throw new IllegalArgumentException("Wrong class for "+a);
    }
}
