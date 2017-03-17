package com.vocalabs.egtest.testcase;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Provides data obtained by SelfTestMessageHandler */
public class Messages {
    private static Map<String,String> unsupportedElements = null;
    private static Map<String,String> errorElements = null;

    @Nullable
    public static synchronized String unsupportedReason(String elementKey) {
        if (unsupportedElements == null)
            unsupportedElements = mapForResourceName("unsupportedElements.txt");
        return unsupportedElements.get(elementKey);
    }

    @Nullable
    public static synchronized String errorReason(String elementKey) {
        if (errorElements == null)
            errorElements = mapForResourceName("errorElements.txt");
        return errorElements.get(elementKey);
    }

    private static Map<String,String> mapForResourceName(String resourceName) {
        InputStream inputStream = Messages.class.getClassLoader().getResourceAsStream(resourceName);
        List<String> items = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.toList());
        Map<String,String> map = new HashMap<>();
        for (String item: items) {
            String[] a = item.split("\t");
            map.put(a[0], a[1]);
        }
        return Collections.unmodifiableMap(map);
    }
}
