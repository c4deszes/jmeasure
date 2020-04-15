package org.jmeasure.core.visa.factory;

import java.util.function.Function;

/**
 * InstrumentNameProvider
 */
public interface InstrumentNameProvider extends Function<String, String> {

    /**
     * Provides a suitable name for an instrument
     */
    String apply(String t);
}