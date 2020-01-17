package org.jmeasure.core.signal.analog;

import java.util.function.Function;
import org.jmeasure.core.signal.Signal;

/**
 * AnalogSignal
 */
public class AnalogSignal extends Signal<Float> {

	public AnalogSignal(String id) {
		super(id);
	}

	public AnalogSignal(Signal<Float> signal, Function<Float, Float> transform) {
		super(signal, transform);
	}

	public double amplitude() {
		return max() - min();
	}
	
	public double offset() {
		return (max() + min()) / 2.0;
	}
	
}