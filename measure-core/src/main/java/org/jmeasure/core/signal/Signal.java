package org.jmeasure.core.signal;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 
 * @param <T>
 */
public class Signal<T extends Comparable<T>> {
	
	private String id;
	
	private TreeSet<DataPoint<T>> data = new TreeSet<DataPoint<T>>((a, b) -> Float.compare(a.time, b.time));
	
	public Signal(String id) {
		this.id = id;
	}

	public Signal(Signal<T> signal) {
		this(signal.getId());
		this.data.addAll(signal.getData());
	}

	public <U extends Comparable<U>> Signal(Signal<U> signal, Function<U, T> transform) {
		this(signal.getId());
		signal.stream().map(p -> new DataPoint<T>(p.time, transform.apply(p.value))).forEach(this::add);
	}
	
	public Set<DataPoint<T>> getData() {
		return data;
	}

	public String getId() {
		return id;
	}
	
	public void add(DataPoint<T> dp) {
		this.data.add(dp);
	}
	
	public void add(float time, T value) {
		this.data.add(new DataPoint<T>(time, value));
	}

	public Stream<DataPoint<T>> stream() {
		return this.data.stream();
	}

	public T first() {
		return this.data.first().value;
	}

	public T last() {
		return this.data.last().value;
	}
	
	public float period() {
		return data.last().time;
	}

	public T min() {
		return Collections.min(data).value;
	}

	public T max() {
		return Collections.max(data).value;
	}

	public static class DataPoint<T extends Comparable<T>> implements Comparable<DataPoint<T>> {
		public final float time;
		public final T value;

		public DataPoint(float time, T value) {
			this.time = time;
			this.value = value;
		}
		
		@Override
		public int compareTo(DataPoint<T> arg0) {
			return this.value.compareTo(arg0.value);
		}
	}
}
