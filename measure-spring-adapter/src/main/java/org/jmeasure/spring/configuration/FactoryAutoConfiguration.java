package org.jmeasure.spring.configuration;

import java.util.List;

import org.jmeasure.core.lxi.raw.RawSocketFactory;
import org.jmeasure.core.scpi.factory.ISCPIDeviceFactory;
import org.jmeasure.core.scpi.factory.ISCPISocketFactory;
import org.jmeasure.core.scpi.factory.SCPIDeviceFactory;
import org.jmeasure.core.scpi.factory.SCPISocketFactory;
import org.jmeasure.core.scpi.socket.RawSCPISocketFactory;
import org.jmeasure.core.visa.factory.ISocketFactory;
import org.jmeasure.core.visa.factory.SocketFactory;
import org.jmeasure.siglent.SiglentDeviceFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	public ISocketFactory rawSocket() {
		return new RawSocketFactory();
	}

	@Bean
	public ISCPISocketFactory rawSCPISocketFactory() {
		return new RawSCPISocketFactory();
	}

	@Bean
	public SCPIDeviceFactory deviceFactory(
		@Autowired(required = false) SocketFactory socketFactory,
		@Autowired(required = false) SCPISocketFactory scpiFactory, 
		List<ISCPIDeviceFactory> factories) {
		return new SCPIDeviceFactory(socketFactory, scpiFactory, factories);
	}

	@Bean
	public SocketFactory socketFactory(List<ISocketFactory> factories){
		return new SocketFactory(factories);
	}
	
}