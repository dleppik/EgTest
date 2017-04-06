package com.vocalabs.egtest.language;

import java.util.List;

public interface LanguageSupport {
    Object eval(List<String> imports, String text);
}
