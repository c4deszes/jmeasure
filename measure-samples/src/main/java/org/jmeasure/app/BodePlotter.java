package org.jmeasure.app;

import java.io.IOException;

import org.jmeasure.core.device.Oscilloscope;
import org.jmeasure.core.device.WaveformGenerator;
import org.jmeasure.core.signal.Waveform;
import org.jmeasure.core.signal.Waveform.WaveformParameter;
import org.jmeasure.core.signal.Waveform.WaveformType;
import org.jmeasure.lxi.socket.RawSCPISocket;

public class BodePlotter {

	private WaveformGenerator wavegen;

	private Oscilloscope scope;

	private float amplitude;

	private float impedance;

	public BodePlotter(final WaveformGenerator wavegen, final Oscilloscope scope) {
		this.wavegen = wavegen;
		this.scope = scope;
	}

	public void run(float start, float stop, float step) {
		float current = start;
		while(current < stop) {
			Waveform waveform = Waveform.builder()
										.type(WaveformType.SINE)
										.param(WaveformParameter.FREQUENCY, current);
			

			current += step;
		}
		
	}
}