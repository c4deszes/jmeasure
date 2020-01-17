package org.jmeasure.lxi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * LXIDeviceInfo
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class DeviceIdentifier {
	private String manufacturer;
	private String model;
	private String serialNumber;
	private String firmwareVersion;

	/**
	 * 
	 * @param raw
	 * @return
	 */
	public static DeviceIdentifier from(String raw) {
		String[] info = raw.split(",");
		if(info.length != 4) {
			throw new IllegalArgumentException("Device info must be 4 comma-separated fields as per IEEE 488.2");
		}
		return new DeviceIdentifier(info[0], info[1], info[2], info[3]);
	}

	/**
	 * 
	 * @param manufacturer
	 * @param model
	 * @param serialNumber
	 * @param firmwareVersion
	 * @return
	 */
	public static DeviceIdentifier from(String manufacturer, String model, String serialNumber, String firmwareVersion) {
		return new DeviceIdentifier(manufacturer, model, serialNumber, firmwareVersion);
	}

	/**
	 * Returns the string representation of this identifier
	 * @return
	 */
	public String value() {
		return manufacturer + "," + model + "," + serialNumber + "," + firmwareVersion;
	}

	/**
	 * Returns a string representing this identifier in a debug friendly way
	 * @return
	 */
	@Override
	public String toString() {
		return "dev(mf=" + manufacturer + ";model=" + model + ";sn=" + serialNumber + ";ver=" + firmwareVersion + ")";
	}
}