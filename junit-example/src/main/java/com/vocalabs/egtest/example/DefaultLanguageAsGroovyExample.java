package com.vocalabs.egtest.example;

import com.vocalabs.egtest.annotation.Eg;
import com.vocalabs.egtest.annotation.EgDefaultLanguage;
import com.vocalabs.egtest.annotation.EgLanguage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@EgDefaultLanguage(EgLanguage.GROOVY)
public class DefaultLanguageAsGroovyExample {
    @Eg(  given = "['Elephant', 'Octopus', 'Noodles']",
        returns = "['Eea', 'Oou', 'oe']")
    public static List<String> vowels(Collection<String> words) {
        return words.stream()
                .map(GroovyExample::vowels)
                .map(GroovyExample::charsToString)
                .collect(Collectors.toList());
    }


    private final Comparator<String> comparator;

    public DefaultLanguageAsGroovyExample(Comparator<String> comparator) {
        this.comparator = comparator;
    }

    @Eg(construct = {"String.CASE_INSENSITIVE_ORDER"},
            given = "['Europa', 'Elephant', 'Octopus', 'Noodles']",
           returns = "['aEou', 'aEe', 'Oou', 'eo']")
    public List<String> sortedVowels(Collection<String> words) {
        return vowels(words).stream()
                .map(this::sortChars)
                .collect(Collectors.toList());
    }

    private String sortChars(String s) {
        return s.chars()
                .mapToObj(c -> ""+(char)c)
                .sorted(comparator)
                .collect(Collectors.joining(""));
    }
}
