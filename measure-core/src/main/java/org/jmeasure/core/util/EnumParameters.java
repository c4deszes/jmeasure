package org.jmeasure.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * EnumParameters
 */
public class EnumParameters<T extends Enum<T>> {

	private Map<T, Object> params = new HashMap<>();

	public Set<T> getParameters() {
		return params.keySet();
	}

	public boolean assertAsFloat(T param, Predicate<Float> predicate) {
		try {
			return predicate.test((float)params.get(param));
		} catch(ClassCastException e) {
			return false;
		}
	}

	public float getFloat(T param) {
		return (float) params.get(param);
	}

	public int getInt(T param) {
		return (int) params.get(param);
	}

	public boolean getBoolean(T param) {
		return (boolean) params.get(param);
	}

	protected Object put(T param, Object value) {
		return params.put(param, value);
	}

	public boolean has(T param) {
		return params.containsKey(param);
	}

	@SuppressWarnings("unchecked")
	public <U> Optional<U> get(T param) {
		try {
			Object o = params.get(param);
			return Optional.of((U) o);
		} catch(ClassCastException e) {
			return Optional.empty();
		}
	}
}