package org.jmeasure.core.signal.analog.sampler;

import org.jmeasure.core.signal.Signal;
import org.jmeasure.core.signal.analog.AnalogSignal;
import org.jmeasure.core.signal.sampler.Sampler;

/**
 * BinaryToAnalog
 */
public class BinaryToAnalog implements Sampler<Boolean, AnalogSignal> {

	private float high;

	private float low;

	public BinaryToAnalog(float high, float low) {
		this.high = high;
		this.low = low;
	}

	@Override
	public AnalogSignal sample(Signal<Boolean> signal) {
		AnalogSignal out = new AnalogSignal(signal.getId());
		signal.stream()
				.map(dp -> new Signal.DataPoint<Float>(dp.time, dp.value ? high : low))
				.forEach(out::add);
		return out;
	}

	
}