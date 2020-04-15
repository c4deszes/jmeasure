package org.jmeasure.test.instrument;

/**
 * Used to inject inputs and outputs in the JMeasureRunner
 */
public @interface Channel {
    int port() default -1;
    String qualifier() default "";
}