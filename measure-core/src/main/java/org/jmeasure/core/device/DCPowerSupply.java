package org.jmeasure.core.device;

import org.jmeasure.core.util.EnumParameters;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

/**
 * A DC power supply is a device capable of outputting a set voltage or a set current
 * 
 * @author Balazs Eszes
 */
public interface DCPowerSupply {

	/**
	 * Returns the number of power outputs
	 * 
	 * @return Number of power outputs
	 */
	int getPowerOutputCount();

	/**
	 * Sets the output configuration for the given channel
	 * 
	 * @param channel Output channel
	 * @param configuration Output configuration
	 */
	void setPowerOutput(int channel, PowerOutputConfiguration configuration) throws IOException; 

	/**
	 * Returns current output configuration, set voltage, set current, etc.
	 * 
	 * @param channel Output channel
	 * @return Output configuration
	 */
	PowerOutputConfiguration getPowerOutput(int channel) throws IOException;

	/**
	 * Returns whether this device supports a particular parameter for an analog input
	 * 
	 * <p>For example, {@code supports(1, PowerOutputParameter.CURRENT, 2.0f)} should return {@code true} if
	 * the device is capable of outputting 2 Amps
	 * 
	 * @param channel Output channel
	 * @param param Parameter type
	 * @param value Parameter's intended value
	 * @return {@code true} if the value is supported
	 */
	default boolean supports(int channel, PowerOutputParameter param, Object value) {
		return false;
	}

	/**
	 * Returns the output state, including voltage, current, etc.
	 * 
	 * @param channel Output channel
	 * @return Output state
	 */
	PowerState getPowerState(int channel) throws IOException;

	public static class PowerOutputConfiguration extends EnumParameters<PowerOutputParameter> {

		/**
		 * 
		 * @return
		 */
		public static PowerOutputConfiguration defaultBuilder() {
			return new PowerOutputConfiguration().voltage(0).current(0).ovpState(false);
		}

		public static PowerOutputConfiguration builder() {
			return new PowerOutputConfiguration();
		}

		public PowerOutputConfiguration voltage(float voltage) {
			this.put(PowerOutputParameter.VOLTAGE, voltage);
			return this;
		}

		public PowerOutputConfiguration current(float current) {
			this.put(PowerOutputParameter.CURRENT, current);
			return this;
		}

		public PowerOutputConfiguration overvoltage(float voltage) {
			this.put(PowerOutputParameter.OVP_VOLTAGE, voltage);
			this.put(PowerOutputParameter.OVP_ENABLED, true);
			return this;
		}

		public PowerOutputConfiguration ovpState(boolean enabled) {
			this.put(PowerOutputParameter.OVP_ENABLED, enabled);
			return this;
		}

	}

	public static enum PowerOutputParameter {
		VOLTAGE, CURRENT, OVP_VOLTAGE, OVP_ENABLED
	}

	@AllArgsConstructor
	public static class PowerState {

		@Getter
		private final float voltage;

		@Getter
		private final float current;

		@Getter
		private final OutputMode mode;

		public static enum OutputMode {
			CONSTANT_CURRENT, CONSTANT_VOLTAGE;
		}

	}
}