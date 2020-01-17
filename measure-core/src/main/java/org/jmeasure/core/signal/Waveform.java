package org.jmeasure.core.signal;

import org.jmeasure.core.util.EnumParameters;

import lombok.Getter;

import org.jmeasure.core.signal.Waveform.WaveformParameter;

public class Waveform extends EnumParameters<WaveformParameter> {

	@Getter
	private WaveformType type;

	public static Waveform builder() {
		return new Waveform();
	}

	public Waveform type(WaveformType type) {
		this.type = type;
		return this;
	}

	public Waveform param(WaveformParameter param, Object value) {
		this.put(param, value);
		return this;
	}

	public static enum WaveformParameter {
		PERIOD, FREQUENCY, AMPLITUDE, OFFSET, DUTY, SYMMETRY, PHASE, STDEV, MEAN, WIDTH, RISE, FALL, DELAY;
	}

	public static enum WaveformType {
		SINE, SQUARE, RAMP, PULSE, NOISE, DC;
	}

}