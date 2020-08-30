package org.jmeasure.spring.configuration;

import java.util.List;

import org.jmeasure.core.lxi.raw.RawSocketFactory;
import org.jmeasure.core.lxi.vxi11.VXI11SocketFactory;
import org.jmeasure.core.visa.factory.ISocketFactory;
import org.jmeasure.core.visa.factory.SocketFactory;
import org.jmeasure.core.visa.mock.MockSocketFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketFactoryAutoConfiguration {

	@Bean
	public ISocketFactory rawSocketFactory() {
		return new RawSocketFactory();
	}

	@Bean
	public ISocketFactory vxi11SocketFactory() {
		return new VXI11SocketFactory();
	}

	@Bean
	public ISocketFactory mockSocketFactory() {
		return new MockSocketFactory();
	}

	@Bean
	public SocketFactory socketFactory(List<ISocketFactory> factories){
		return new SocketFactory(factories);
	}
	
}