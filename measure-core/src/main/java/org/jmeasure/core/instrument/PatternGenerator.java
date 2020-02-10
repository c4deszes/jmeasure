package org.jmeasure.core.instrument;

import org.jmeasure.core.signal.Signal;
import org.jmeasure.core.util.EnumParameters;

import java.io.IOException;

/**
 * A pattern generator is a device capable of outputting binary signals
 * 
 * @author Balazs Eszes
 */
public interface PatternGenerator extends AutoCloseable {

	public static final DigitalOutputConfiguration CMOS_5V = DigitalOutputConfiguration.builder().enabled().high(5.0f).low(0.0f);

	public static final DigitalOutputConfiguration CMOS_3V3 = DigitalOutputConfiguration.builder().enabled().high(3.3f).low(0.0f);

	/**
	 * Returns the number of available output channels
	 * 
	 * @return Number of output channels
	 */
	int getDigitalOutputCount();
	
	/**
	 * Sets the digital output settings for the given channel
	 * 
	 * @param channel Digital output channel
	 * @param configuration Channel settings
	 * @throws Exception
	 */
	void setDigitalOutput(int channel, DigitalOutputConfiguration configuration) throws IOException;

	/**
	 * Returns 
	 * 
	 * @param channel
	 * @return
	 */
	DigitalOutputConfiguration getDigitalOutput(int channel) throws IOException;

	/**
	 * Sets the output signal on the given channel
	 * 
	 * @param channel Digital output channel
	 * @param signal Digital signal
	 */
	void setDigitalSignal(int channel, Signal<Boolean> signal) throws IOException;

	/**
	 * Returns whether this device supports a particular parameter for a digital output
	 * 
	 * <p>For example, {@code supports(1, DigitalOutputParameter.HIGH, 5.0f)} should return {@code true} if
	 * the device is capable of outputting 5V as digital high
	 * 
	 * @param channel Digital output channel
	 * @param param Parameter type
	 * @param value Parameter's intended value
	 * @return {@code true} if the value is supported
	 */
	default boolean supports(int channel, DigitalOutputParameter param, Object value) {
		return false;
	}

	public static class DigitalOutputConfiguration extends EnumParameters<DigitalOutputParameter> {
	
		private DigitalOutputConfiguration() {
			
		}

		public static DigitalOutputConfiguration builder() {
			return new DigitalOutputConfiguration();
		}

		public DigitalOutputConfiguration state(boolean enabled) {
			this.put(DigitalOutputParameter.ENABLED, enabled);
			return this;
		}

		public DigitalOutputConfiguration enabled() {
			return this.state(true);
		}

		public DigitalOutputConfiguration disabled() {
			return this.state(false);
		}

		public DigitalOutputConfiguration high(float high) {
			this.put(DigitalOutputParameter.HIGH, high);
			return this;
		}

		public DigitalOutputConfiguration low(float low) {
			this.put(DigitalOutputParameter.LOW, low);
			return this;
		}
	}

	public static enum DigitalOutputParameter {
		ENABLED, HIGH, LOW, SLEW, LOAD
	}
}