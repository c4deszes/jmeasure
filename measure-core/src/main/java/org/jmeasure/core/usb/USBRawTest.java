package org.jmeasure.core.usb;

import org.usb4java.ConfigDescriptor;
import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

public class USBRawTest {
	
	public static void main(String[] args) {
		Context context = new Context();
		int result = LibUsb.init(context);
		if (result != LibUsb.SUCCESS) {
			throw new LibUsbException("Unable to initialize libusb.", result);
		}
		System.out.println(LibUsb.getApiVersion());

		DeviceList list = new DeviceList();
		result = LibUsb.getDeviceList(context, list);
		if (result < 0) throw new LibUsbException("Unable to get device list", result);
	
		try
		{
			// Iterate over all devices and scan for the right one
			for (Device device: list)
			{
				DeviceDescriptor descriptor = new DeviceDescriptor();
				result = LibUsb.getDeviceDescriptor(device, descriptor);
				if (result != LibUsb.SUCCESS) throw new LibUsbException("Unable to read device descriptor", result);
				//System.out.println(descriptor);
				if(descriptor.idVendor() == (short)0xf4ec && descriptor.idProduct() == (short)0xee38) {
					System.out.println(descriptor);
					for(byte i = 0; i < descriptor.bNumConfigurations(); i++) {
						ConfigDescriptor configDescriptor = new ConfigDescriptor();
						result = LibUsb.getConfigDescriptor(device, (byte)0, configDescriptor);
						System.out.println(configDescriptor);
						LibUsb.freeConfigDescriptor(configDescriptor);
					}
				}
			}
		}
		finally
		{
			// Ensure the allocated device list is freed
			LibUsb.freeDeviceList(list, true);
		}

		LibUsb.exit(context);
	}
}