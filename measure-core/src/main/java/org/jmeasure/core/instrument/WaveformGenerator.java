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
public interface WaveformGenerator extends AutoCloseable {

	/**
	 * <b>High impedance mode</b>
	 * 
	 * <p>It's required that the device goes HIZ after {@code setOutputConfiguration(1, OutputConfiguration.builder().load(WaveformGenerator.HIZ))}
	 */
	public static final float HIZ = Float.MAX_VALUE;

	/**
	 * Returns the number of available analog outputs
	 * @return Number of analog outputs
	 */
	int getAnalogOutputCount();

	/**
	 * Changes the output properties of a channel
	 * 
	 * @param channel Output channel number
	 * @param output
	 * @throws Exception
	 */
	void setOutputConfiguration(int channel, OutputConfiguration output) throws IOException;

	/**
	 * Changes the output of the waveform generator to an arbitrary waveform
	 * 
	 * @param channel Output channel
	 * @param signal Arbitrary waveform
	 */
	void setAnalogSignal(int channel, Signal<Float> signal) throws IOException;

	/**
	 * Returns the arbitrary waveform stored on the device
	 * 
	 * @param id Waveform's id
	 * @return Analog waveform, empty if signal doesn't exist
	 */
	Optional<AnalogSignal> getAnalogSignal(String id);

	/**
	 * Changes the output of the waveform generator to a builtin waveform
	 * 
	 * @param channel Output channel
	 * @param waveform Builtin waveform
	 */
	void setAnalogWaveform(int channel, Waveform waveform) throws IOException;

	/**
	 * Returns the builtin waveform currently active on the channel
	 * 
	 * @param channel Output channel
	 * @return Optional waveform
	 */
	Optional<Waveform> getAnalogWaveform(int channel);

	/**
	 * Returns whether this device supports a particular value for it's output configuration
	 * 
	 * <p>For example {@code supports(1, OutputParameter.LOAD, 50)} should return true if the 
	 * device has a 50Ohm output impedance mode on Channel 1
	 * 
	 * @param parameter Output parameter type
	 * @param value Parameter's intended value
	 * @return {@code true} if the value is supported
	 */
	default boolean supports(int channel, OutputParameter parameter, Object value) {
		return false;
	}

	/**
	 * Returns whether this device supports a particular value for a waveform's parameter
	 * 
	 * <p>For example, {@code supports(1, WaveformType.SINE, WaveformParameter.FREQUENCY, 120 000)}
	 * should return true if the device is capable of outputting 120MHz sine waves on Channel 1
	 * 
	 * <p>Since it's not possible to validate whether certain parameters violate each other, that logic 
	 * should instead be in the {@code setAnalogWaveform} function
	 * 
	 * @param type Waveform type
	 * @param parameter Parameter type
	 * @param value Parameter's intended value
	 * @return {@code true} if the value is supported
	 */
	default boolean supports(int channel, WaveformType type, WaveformParameter parameter, Object value) {
		return false;
	}

	/**
	 * Sets the clock source of the waveform generator
	 * @param source Reference clock source
	 */
	void setClockSource(ClockSource source) throws IOException;

	//void setModulatingWave(int channel, ModulationParameters parameters);

	//void setBurstMode(int channel, )

	//void burstTrigger()

	/**
	 * Output configuration builder class
	 * 
	 * 
	 */
	public static class OutputConfiguration extends EnumParameters<OutputParameter> {

		private OutputConfiguration() {

		}

		public static OutputConfiguration builder() {
			return new OutputConfiguration();
		}

		/**
		 * Sets the output impedance
		 * @param impedance Output impedance
		 * @return OutputCongifuration {@code this}
		 */
		public OutputConfiguration load(float impedance) {
			this.put(OutputParameter.LOAD, impedance);
			return this;
		}

		/**
		 * Sets the output state
		 * @param enabled if true the output is enabled, if false the output is disabled
		 * @return OutputCongifuration {@code this}
		 */
		public OutputConfiguration state(boolean enabled) {
			this.put(OutputParameter.ENABLED, enabled);
			return this;
		}

		/**
		 * Shorthand method for enabling the output, equivalent to {@code config.state(true)}
		 * @return OutputCongifuration {@code this}
		 */
		public OutputConfiguration enabled() {
			return this.state(true);
		}

		/**
		 * Shorthand method for disabling the output, equivalent to {@code config.state(false)}
		 * @return OutputCongifuration {@code this}
		 */
		public OutputConfiguration disabled() {
			return this.state(false);
		}

		/**
		 * Sets the output polarity to inverted
		 * @return OutputCongifuration {@code this}
		 */
		public OutputConfiguration inverted() {
			this.put(OutputParameter.INVERTED, true);
			return this;
		}
	}

	/**
	 * Output parameters
	 */
	public static enum OutputParameter {
		/**
		 * Waveform generator's output state
		 * 
		 * <p>true value means the output should be active, a false value should 
		 * turn the output off
		 * 
		 * <p>You can rely on this value being boolean type
		 */
		ENABLED, 

		/**
		 * Waveform generator's output impedance
		 * 
		 * <p>You can assume this value is float type
		 * 
		 * <p>The value Float.MAX_VALUE must result in a High Impedance state, but 
		 * could turn HiZ at any lower value
		 */
		LOAD,

		/**
		 * Waveform generator's output polarity
		 * 
		 * <p>true value means the output should be inverted
		 * 
		 * <p>You can assume this value is bool
		 */
		INVERTED
	}

	public static enum ClockSource {
		INTERNAL, EXTERNAL
	}
}