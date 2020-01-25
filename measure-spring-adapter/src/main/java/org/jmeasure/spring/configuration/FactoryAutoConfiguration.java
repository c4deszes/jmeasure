package org.jmeasure.spring.configuration;

import java.util.List;

import org.jmeasure.core.scpi.ISCPIDeviceFactory;
import org.jmeasure.core.scpi.SCPIDeviceFactory;
import org.jmeasure.core.visa.factory.ISocketFactory;
import org.jmeasure.core.visa.factory.RawSocketFactory;
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
	public SCPIDeviceFactory deviceFactory(@Autowired SocketFactory socketFactory, List<ISCPIDeviceFactory> factories) {
		return new SCPIDeviceFactory(socketFactory, factories);
	}

	@Bean
	public SocketFactory socketFactory(List<ISocketFactory> factories){
		return new SocketFactory(factories);
	}
	
}