package org.jmeasure.core.signal.analog.sampler;

import org.jmeasure.core.signal.analog.AnalogSampler;
import org.jmeasure.core.signal.analog.AnalogSignal;
import org.jmeasure.core.signal.Signal;

import lombok.Getter;

/**
 * This sampler inverts any analog signal alongside the voltage axis
 */
public class AnalogInverter implements AnalogSampler {

	@Getter
	private final float offset;

	public AnalogInverter() {
		this(0.0f);
	}

	public AnalogInverter(float offset) {
		this.offset = offset;
	}

	@Override
	public AnalogSignal sample(Signal<Float> signal) {
		return new AnalogSignal(signal, value -> -(value - offset));
	}

}