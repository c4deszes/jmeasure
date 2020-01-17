package org.jmeasure.core.signal.analog;

import org.jmeasure.core.signal.analog.AnalogSignal;
import org.jmeasure.core.signal.sampler.Sampler;

/**
 * AnalogSampler takes an analog signal and transforms it into another analog signal
 */
public interface AnalogSampler extends Sampler<Float, AnalogSignal> {
	
}