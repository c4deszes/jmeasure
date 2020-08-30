package org.jmeasure.core.instrument;

import org.jmeasure.core.signal.Signal;
import org.jmeasure.core.signal.Waveform;
import org.jmeasure.core.signal.Waveform.WaveformParameter;
import org.jmeasure.core.signal.Waveform.WaveformType;
import org.jmeasure.core.signal.analog.AnalogSignal;
import org.jmeasure.core.util.EnumParameters;

import java.io.IOException;
import java.util.Optional;

/**
 * A waveform generator is a device capable of outputting a changing voltage signal
 * 
 * @author Balazs Eszes
 */
public interface FunctionGenerator extends AutoCloseable {

	public static interface AnalogOutput {
		String getName();

		void setEnabled(boolean enabled);

		void setOperationMode();

		void setImpedance();
	}
}