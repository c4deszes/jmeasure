package org.jmeasure.core.instrument;

import org.jmeasure.core.signal.digital.BinarySignal;
import org.jmeasure.core.util.EnumParameters;

import java.io.IOException;
import java.util.Optional;
/**
 * A logic analyzer is a device capable of making binary signal measurements over time
 * 
 * @author Balazs Eszes
 */
public interface LogicAnalyzer extends TimeDomainAnalyzer, AutoCloseable {

	public final static float TTL = 1.5f;

	public final static float CMOS = 1.65f;

	public final static float LVCMOS3V3 = 1.65f;

	public final static float LVCMOS2V5 = 1.25f;

	/**
	 * Returns the number of available digital channels
	 * 
	 * @return Number of input channels
	 */
	int getDigitalInputCount();

	/**
	 * Sets the digital input channel settings for the given channel
	 * 
	 * @param channel Digital input channel
	 * @param configuration Channel settings
	 */
	void setDigitalInput(int channel, DigitalInputConfiguration configuration) throws IOException;

	/**
	 * Returns the current configuration for the given channel
	 * @param channel Digital input channel
	 * @return Channel settings
	 */
	DigitalInputConfiguration getDigitalInput(int channel) throws IOException;

	/**
	 * Returns the current binary signal recorded by this device
	 * 
	 * <p>It may set the trigger state to STOPPED for the duration of this method, but it has to restore the previous state
	 * 
	 * @param channel Digital input channel
	 * @return Binary signal recorded, empty if no signal present
	 */
	Optional<BinarySignal> getDigitalSignal(int channel) throws IOException;

	/**
	 * Sets the trigger of this device
	 * 
	 * @param trigger Trigger settings
	 */
	void setDigitalTrigger(Trigger.Settings trigger) throws IOException;

	/**
	 * Returns whether this device supports a particular parameter for a digital input
	 * 
	 * <p>For example, {@code supports(1, DigitalInputParameter.THRESHOLD, 1.65f)} should return {@code true} if
	 * the device is capable of setting the threshold voltage on Channel1 to 1.65V
	 * 
	 * @param channel Digital input channel
	 * @param param	Parameter type
	 * @param value Parameter's intended value
	 * @return {@code true} if the value is supported
	 */
	default boolean supports(int channel, DigitalInputParameter param, Object value) {
		return false;
	}

	/**
	 * 
	 */
	public static class DigitalInputConfiguration extends EnumParameters<DigitalInputParameter> {

		private DigitalInputConfiguration() {

		}

		public static DigitalInputConfiguration builder() {
			return new DigitalInputConfiguration();
		}

		/**
		 * 
		 * @param value
		 * @return
		 */
		public DigitalInputConfiguration threshold(float value) {
			this.put(DigitalInputParameter.THRESHOLD, value);
			return this;
		}

		public DigitalInputConfiguration state(boolean enabled) {
			this.put(DigitalInputParameter.ENABLED, enabled);
			return this;
		}

		public DigitalInputConfiguration enabled() {
			return this.state(true);
		}

		public DigitalInputConfiguration disabled() {
			return this.state(false);
		}
	}

	public static enum DigitalInputParameter {
		ENABLED, THRESHOLD
	}
}