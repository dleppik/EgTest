package com.vocalabs.egtest.example;

import com.vocalabs.egtest.annotation.Eg;
import com.vocalabs.egtest.annotation.EgLanguage;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Example of EgTests which are better suited for Groovy.
 */
public class GroovyExample {


    @Eg( language = EgLanguage.GROOVY,
            given = "['Elephant', 'Octopus', 'Noodles']", returns = "['Eea', 'Oou', 'oe']")
    public static List<String> vowels(Collection<String> words) {
        return words.stream()
                .map(GroovyExample::vowels)
                .map(GroovyExample::charsToString)
                .collect(Collectors.toList());
    }

    /** Returns a sorted set of vowels for each of the words */
    private static Set<Character> vowels(String words) {
        return words
                .chars()
                .filter(it -> isVowel((char) it))
                .mapToObj(it -> (char)it)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    @Eg(language = EgLanguage.GROOVY, given = "'a'", returns = "true")
    public static boolean isVowel(char ch) {
        switch (Character.toLowerCase(ch)) {
            case 'a': return true;
            case 'e': return true;
            case 'i': return true;
            case 'o': return true;
            case 'u': return true;
            default:  return false;
        }
    }

    @Eg(language = EgLanguage.GROOVY, given = {"3", "[2.0f, 5f]"}, returns = "3.3333", delta = 0.001)
    public static float divide(float denominator, List<Float> numerators) {
        if (numerators.isEmpty())
            return 0;
        float num = 1;
        for (Float numerator : numerators) {
            num *= numerator;
        }
        return num / denominator;
    }

    private static String charsToString(Set<Character> chars) {
        StringBuilder sb = new StringBuilder(chars.size());
        for (Character ch : chars) {
            sb.append(ch);
        }
        return sb.toString();
    }

    private final Comparator<String> comparator;

    public GroovyExample(Comparator<String> comparator) {
        this.comparator = comparator;
    }

    @Eg( language = EgLanguage.GROOVY,
        construct = {"String.CASE_INSENSITIVE_ORDER"},
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
