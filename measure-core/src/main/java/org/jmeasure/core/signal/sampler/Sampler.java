package org.jmeasure.core.signal.sampler;

import java.util.function.Function;
import org.jmeasure.core.signal.Signal;

/**
 * A sampler 
 */
public interface Sampler<V extends Comparable<V>, U extends Signal<? extends Comparable<?>>> extends Function<Signal<V>, U> {

	public U sample(Signal<V> signal);

	@Override
	default U apply(Signal<V> t) {
		return sample(t);
	}

}