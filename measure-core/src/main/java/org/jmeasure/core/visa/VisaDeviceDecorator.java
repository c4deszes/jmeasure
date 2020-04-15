package org.jmeasure.core.visa;

import java.util.function.Function;

/**
 * VisaDeviceDecorator
 */
public interface VisaDeviceDecorator<T> extends Function<Object, T> {

    boolean supports(Object device);
}