package org.jmeasure.core.signal.digital;

import org.jmeasure.core.signal.digital.BinarySignal;
import org.jmeasure.core.signal.Signal;

/**
 * BinaryInverter takes a binary signal and inverts the signal logically
 */
public class BinaryInverter implements BinarySampler {

	@Override
	public BinarySignal sample(Signal<Boolean> signal) {
		BinarySignal output = new BinarySignal("~" + signal.getId());
		signal.stream().forEach(point -> {
			output.add(point.time, !point.value);
		});
		return output;
	}
	
}