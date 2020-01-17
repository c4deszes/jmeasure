package org.jmeasure.core.util;

/**
 * Util
 */
public final class Util {

	private static final float MEGA = 1000000.0f;

	private static final float KILO = 1000.0f;

	private static final float MILLI = 0.001f;

	private static final float MICRO = 0.000001f;

	private static final float NANO = 0.000000001f;

	public static float mega(float value) {
		return MEGA * value;
	}

	public static float kilo(float value) {
		return KILO * value;
	}

	public static float milli(float value) {
		return MILLI * value;
	}

	public static float micro(float value) {
		return MICRO * value;
	}

	public static float nano(float value) {
		return NANO * value;
	}

	public static float nV(float nanovolts) {
		return nano(nanovolts);
	}

	public static float mV(float millivolts) {
		return milli(millivolts);
	}
}