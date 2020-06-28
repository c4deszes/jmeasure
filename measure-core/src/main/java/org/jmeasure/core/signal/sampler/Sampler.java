package org.jmeasure.core.signal.sampler;

import java.util.function.Function;
import org.jmeasure.core.signal.Signal;

/**
 * A sampler 
 */
public interface Sampler<V extends Comparable<V>, U extends Signal<? extends Comparable<?>>> {

	public U sample(Signal<V> signal);

}