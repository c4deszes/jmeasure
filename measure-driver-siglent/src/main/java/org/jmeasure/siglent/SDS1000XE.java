package org.jmeasure.siglent;

import java.io.IOException;

import org.jmeasure.core.device.LogicAnalyzer;
import org.jmeasure.core.device.Oscilloscope;
import org.jmeasure.core.device.WaveformGenerator;
import org.jmeasure.core.signal.Signal;
import org.jmeasure.core.signal.Waveform;
import org.jmeasure.core.signal.analog.AnalogSignal;
import org.jmeasure.core.signal.digital.BinarySignal;
import org.jmeasure.lxi.DeviceIdentifier;
import org.jmeasure.lxi.SCPIDevice;
import org.jmeasure.lxi.SCPISocket;

/**
 * SDS1000XE
 */
public class SDS1000XE extends SCPIDevice {

	public SDS1000XE(DeviceIdentifier deviceIdentifier, SCPISocket socket) throws IOException {
		super(deviceIdentifier, socket);
	}

	
}