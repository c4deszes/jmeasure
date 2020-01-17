package org.jmeasure.core.device;

import org.jmeasure.core.signal.analog.AnalogSignal;
import org.jmeasure.core.util.EnumParameters;

import java.io.IOException;
import java.util.Optional;

/**
 * An oscilloscope is a device capable of making voltage measurements over time
 * 
 * @author Balazs Eszes
 */
public interface Oscilloscope extends TimeDomainAnalyzer {

	/**
	 * Returns the number of available analog channels
	 * 
	 * @return Number of analog channels
	 */
	int getAnalogInputCount();

	/**
	 * Sets the analog input settings for the given channel
	 * 
	 * @param channel Analog input channel
	 * @param configuration Channel settings
	 */
	void setAnalogInput(int channel, AnalogInputConfiguration configuration) throws IOException;

	/**
	 * Returns the current configuration for the given channel
	 * @param channel Analog input channel
	 * @return Channel settings;
	 */
	AnalogInputConfiguration getAnalogInput(int channel) throws IOException;

	/**
	 * Returns the current analog signal recorded by the device
	 * 
	 * <p>It may set the trigger state to STOPPED for the duration of this method, but it has to restore the previous state
	 * 
	 * @param channel Analog input channel
	 * @return Analog signal recorded
	 */
	Optional<AnalogSignal> getAnalogSignal(int channel) throws IOException;

	/**
	 * Sets the trigger of this device
	 * @param trigger Trigger settings
	 */
	void setAnalogTrigger(Trigger.Settings trigger) throws IOException;

	/**
	 * Returns whether this device supports a particular parameter for an analog input
	 * 
	 * <p>For example, {@code supports(1, AnalogChannelParameter.LOAD, 50.0)} should return {@code true} if
	 * the device is capable of having a 50Ohm input mode
	 * 
	 * @param channel Analog input channel
	 * @param param Parameter type
	 * @param value Parameter's intended value
	 * @return {@code true} if the value is supported
	 */
	default boolean supports(int channel, AnalogInputParameter param, Object value) {
		return false;
	}

	public static class AnalogInputConfiguration extends EnumParameters<AnalogInputParameter> {

	}

	public static enum AnalogInputParameter {
		ENABLED, SCALE, OFFSET, COUPLING, PROBE_ATTENUATION, LOAD
	}
}