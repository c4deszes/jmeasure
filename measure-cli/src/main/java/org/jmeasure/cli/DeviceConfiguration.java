package org.jmeasure.cli;

import java.util.List;

import org.jmeasure.lxi.SCPIDeviceFactory;
import org.jmeasure.lxi.SCPISocketFactory;
import org.jmeasure.lxi.SCPIDeviceFactory.ISCPIDeviceFactory;
import org.jmeasure.lxi.SCPISocketFactory.ISCPISocketFactory;
import org.jmeasure.lxi.socket.RawSCPISocketFactory;
import org.jmeasure.siglent.SiglentDeviceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DeviceConfiguration
 */
@Configuration
public class DeviceConfiguration {

	@Bean
	public ISCPIDeviceFactory siglent() {
		return new SiglentDeviceFactory();
	}

	@Bean
	public ISCPISocketFactory<?> rawSocket() {
		return new RawSCPISocketFactory();
	}

	@Bean
	public SCPIDeviceFactory deviceFactory(List<ISCPIDeviceFactory> factories) {
		SCPIDeviceFactory factory = new SCPIDeviceFactory(1000);
		factories.forEach(factory::add);
		return factory;
	}

	@Bean
	public SCPISocketFactory socketFactory(List<ISCPISocketFactory<?>> factories){
		SCPISocketFactory factory = new SCPISocketFactory();
		factories.forEach(factory::add);
		return factory;
	}
	
}