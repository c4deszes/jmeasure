package org.jmeasure.core.visa;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Device identifier, specified in the SCPI-99 standard
 * 
 * <p>
 * Includes the device vendor, model, serial number and firmware version
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class DeviceIdentifier {

	public final static DeviceIdentifier UNKNOWN = new DeviceIdentifier("-", "-", "-", "-");

	private final String manufacturer;
	private final String model;
	private final String serialNumber;
	private final String firmwareVersion;

	/**
	 * Constructs an Identifier from the format seen in the LXI Discovery and Identification specification
	 * 
	 * @param raw Raw string in the 
	 * @return Device identifier object
	 */
	public static DeviceIdentifier from(String raw) {
		String[] info = raw.split(",");
		if(info.length != 4) {
			throw new IllegalArgumentException("bad identifier: " + raw + " must be 4 comma-separated fields");
		}
		return new DeviceIdentifier(info[0], info[1], info[2], info[3]);
	}

	/**
	 * Constructs an Identifier from the fields found in LXI Discovery and Identification specification
	 * 
	 * @param manufacturer Manufacturer of the device
	 * @param model Model number
	 * @param serialNumber Serial number
	 * @param firmwareVersion Firmware version
	 * @return Device identifier object
	 */
	public static DeviceIdentifier from(String manufacturer, String model, String serialNumber, String firmwareVersion) {
		return new DeviceIdentifier(manufacturer, model, serialNumber, firmwareVersion);
	}

	/**
	 * Returns the string representation of this identifier
	 * @return Raw string identifier
	 */
	public String value() {
		return manufacturer + "," + model + "," + serialNumber + "," + firmwareVersion;
	}

	/**
	 * Returns a string representing this identifier in a debug friendly way
	 * @return Debug string
	 */
	@Override
	public String toString() {
		return "[mf=" + manufacturer + ";model=" + model + ";sn=" + serialNumber + ";ver=" + firmwareVersion + "]";
	}
}