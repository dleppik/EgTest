package com.vocalabs.egtest.processor.selftest;

import com.vocalabs.egtest.processor.AnnotationCollector;
import com.vocalabs.egtest.processor.MessageHandler;
import com.vocalabs.egtest.processor.data.EgItem;
import com.vocalabs.egtest.processor.JavaModelUtil;

import javax.lang.model.element.Element;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SelfTestAnnotationCollector extends AnnotationCollector {
    public SelfTestAnnotationCollector(MessageHandler messageHandler) {
        super(messageHandler);
    }

    private static final HashMap<String, ExpectedBehavior> behaviorAnnotations = new HashMap<>();

    @Override
    public void add(EgItem<?> data) {
        Element el = data.getElement();
        EgSelfTest selfTest = el.getAnnotation(EgSelfTest.class);
        ExpectedBehavior expectedBehavior = (selfTest==null) ? ExpectedBehavior.CREATE_WORKING_TEST : selfTest.value();
        behaviorAnnotations.put(JavaModelUtil.key(data.getElement()), expectedBehavior);
        super.add(data);
    }

    public static Map<String, ExpectedBehavior> getBehaviorAnnotations() {
        return Collections.unmodifiableMap(new TreeMap<>(behaviorAnnotations));
    }
}
