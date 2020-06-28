package org.jmeasure.core.signal.analog.sampler;

import org.jmeasure.core.signal.analog.AnalogSampler;
import org.jmeasure.core.signal.analog.AnalogSignal;

import java.util.function.Function;

import org.jmeasure.core.signal.Signal;
import org.jmeasure.core.signal.Signal.DataPoint;

/**
 * ClippingSampler takes an analog signal and clips voltages to minimum and
 * maximum values.
 */
public class ClippingSampler implements AnalogSampler, Function<Float, Float> {

	private float min;

	private float max;

	/**
	 * Creates a new clipping sampler
	 * 
	 * @param min Minimum voltage
	 * @param max Maximum voltage
	 */
	public ClippingSampler(float min, float max) {
		this.min = min;
		this.max = max;
	}

	@Override
	public Float apply(Float value) {
		return Math.max(Math.min(value, max), min);
	}

	@Override
	public AnalogSignal sample(Signal<Float> signal) {
		return new AnalogSignal(signal, this);
	}
	
}