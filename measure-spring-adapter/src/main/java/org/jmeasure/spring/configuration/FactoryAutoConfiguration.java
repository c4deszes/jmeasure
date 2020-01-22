package org.jmeasure.spring.configuration;

import java.util.List;

import org.jmeasure.lxi.factory.CompositeSCPIDeviceFactory;
import org.jmeasure.lxi.factory.CompositeSCPISocketFactory;
import org.jmeasure.lxi.factory.ISCPIDeviceFactory;
import org.jmeasure.lxi.factory.ISCPISocketFactory;
import org.jmeasure.lxi.socket.RawSCPISocketFactory;
import org.jmeasure.siglent.SiglentDeviceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FactoryAutoConfiguration {

	@Bean
	@ConditionalOnClass(SiglentDeviceFactory.class)
	public ISCPIDeviceFactory siglent() {
		return new SiglentDeviceFactory();
	}

	@Bean
	public ISCPISocketFactory rawSocket() {
		return new RawSCPISocketFactory();
	}

	@Bean
	public CompositeSCPIDeviceFactory deviceFactory(List<ISCPIDeviceFactory> factories) {
		return new CompositeSCPIDeviceFactory(1000, factories);
	}

	@Bean
	public CompositeSCPISocketFactory socketFactory(List<ISCPISocketFactory> factories){
		return new CompositeSCPISocketFactory(factories);
	}
	
}