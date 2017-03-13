package com.vocalabs.egtest.processor.data;

import com.vocalabs.egtest.processor.MessageHandler;
import com.vocalabs.egtest.processor.selftest.EgSelfTest;
import com.vocalabs.egtest.processor.selftest.ExpectedBehavior;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

/** Wraps Examples that are annotated as internal tests of EgTest with {@link EgSelfTest}. */
public class SelfTestExample implements Example<Annotation> {

    static @Nullable ExpectedBehavior expectedBehavior(Element el, boolean selfTest, MessageHandler messageHandler) {
        EgSelfTest egSelfTest = el.getAnnotation(EgSelfTest.class);
        if ( ! selfTest && egSelfTest != null)
            messageHandler.error(el, "EgSelfTest annotation found outside of EgTest self-test");
        return (egSelfTest == null) ? null : egSelfTest.value();
    }


    /** If {@code selfTestAnnotation} is null, return {@code example}; otherwise wrap example in a SelfTestExample. */
    public static Example<?> decorate(Example<?> example, @Nullable ExpectedBehavior expectedBehavior) {
        return (expectedBehavior == null) ? example : new SelfTestExample(expectedBehavior, example);
    }

    private final ExpectedBehavior expectedBehavior;
    private final Example<?> example;

    public SelfTestExample(ExpectedBehavior expectedBehavior, Example<?> example) {
        this.expectedBehavior = expectedBehavior;
        this.example = example;
    }

    /** The internal annotation, not the self-test. */
    @Override
    public Annotation getAnnotation() { return example.getAnnotation(); }

    @Override
    public Element getElement() { return example.getElement(); }

    public ExpectedBehavior getExpectedBehavior() { return expectedBehavior; }
}
