package com.vocalabs.egtest.example;

import com.vocalabs.egtest.annotation.Eg;
import com.vocalabs.egtest.annotation.EgLanguage;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Example of EgTests which are better suited for Groovy.
 */
public class GroovyExample {


    @Eg( language = EgLanguage.GROOVY,
            given = "['Elephant', 'Octopus', 'Noodles']", returns = "['Eae', 'Oou', 'eo']")
    public static List<String> vowels(Collection<String> words) {
        return words.stream()
                .map(GroovyExample::vowels)
                .map(GroovyExample::charsToSortedString)
                .collect(Collectors.toList());
    }

    /** Returns a sorted set of vowels for each of the words */
    private static Set<Character> vowels(String words) {
        return words
                .chars()
                .filter(it -> isVowel((char) it))
                .mapToObj(it -> (char)it)
                .collect(Collectors.toSet());
    }

    private static boolean isVowel(char ch) {
        switch (Character.toLowerCase(ch)) {
            case 'a': return true;
            case 'e': return true;
            case 'i': return true;
            case 'o': return true;
            case 'u': return true;
            default:  return false;
        }
    }

    private static String charsToSortedString(Set<Character> chars) {
        List<Character> sortedChars = new ArrayList<>(chars);
        Collections.sort(sortedChars);
        StringBuilder sb = new StringBuilder(sortedChars.size());
        for (Character ch : sortedChars) {
            sb.append(ch);
        }
        return sb.toString();
    }
}
