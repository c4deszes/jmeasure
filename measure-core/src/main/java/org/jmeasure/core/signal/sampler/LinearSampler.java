package org.jmeasure.core.signal.sampler;

import org.jmeasure.core.signal.Signal;
import org.jmeasure.core.signal.Signal.DataPoint;

/**
 * LinearSampler
 */
public class LinearSampler<U extends Number & Comparable<U>> implements Sampler<U, Signal<U>> {

	@Override
	public Signal<U> sample(Signal<U> signal) {
		return null;
	}

}