package org.jmeasure.core.signal.analog.sampler;

import org.jmeasure.core.signal.analog.AnalogSampler;
import org.jmeasure.core.signal.analog.AnalogSignal;
import org.jmeasure.core.signal.Signal;

import lombok.Getter;

/**
 * ClippingSampler takes an analog signal and clips voltages to minimum and maximum values.
 */
public class ClippingSampler implements AnalogSampler {

	@Getter
	private float min;

	@Getter
	private float max;

	/**
	 * Creates a new clipping sampler
	 * @param min Minimum voltage
	 * @param max Maximum voltage
	 */
	public ClippingSampler(float min, float max) {
		this.min = min;
		this.max = max;
	}	

	@Override
	public AnalogSignal sample(Signal<Float> signal) {
		return new AnalogSignal(signal, value -> Math.max(Math.min(value, max), min));
	}
	
}