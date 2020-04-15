package org.jmeasure.spring.configuration;

import java.util.List;
import java.util.Set;

import org.jmeasure.core.scpi.factory.ISCPIDeviceFactory;
import org.jmeasure.core.scpi.factory.ISCPISocketFactory;
import org.jmeasure.core.scpi.factory.SCPIDeviceFactory;
import org.jmeasure.core.scpi.factory.SCPISocketFactory;
import org.jmeasure.core.scpi.mock.MockSCPIClass;
import org.jmeasure.core.scpi.mock.MockSCPISocket;
import org.jmeasure.core.scpi.mock.MockSCPISocketFactory;
import org.jmeasure.core.scpi.socket.RawSCPISocketFactory;
import org.jmeasure.siglent.SiglentDeviceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * SCPISocketFactoryAutoConfiguration
 */
@Configuration
@Slf4j
public class SCPISocketFactoryAutoConfiguration {

	@Bean
	@Order
	public RawSCPISocketFactory rawSCPISocketFactory() {
		return new RawSCPISocketFactory();
	}

	@Bean
	public MockSCPISocketFactory mockSCPISocketFactory() {
		MockSCPISocketFactory factory = new MockSCPISocketFactory();
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(MockSCPIClass.class));
		
		Set<BeanDefinition> klasses = scanner.findCandidateComponents("org.jmeasure");
		klasses.forEach(def -> {
			try {
				Class<? extends MockSCPISocket> klass = (Class<? extends MockSCPISocket>) Class.forName(def.getBeanClassName());
				String name = klass.getAnnotation(MockSCPIClass.class).value();
				factory.register(name, klass);
				log.debug("Registered MockSCPISocket: name=" + name + ", class=" + klass.getName());
			} catch(Exception e) {
				log.warn("Failed to register " + def.getBeanClassName(), e);
			}
		});
		return factory;
	}

    @Bean
    public SCPISocketFactory scpiSocketFactory(final List<ISCPISocketFactory> factories)  {
        return new SCPISocketFactory(factories);
    }
    
    @Bean
	@ConditionalOnClass(SiglentDeviceFactory.class)
	public SiglentDeviceFactory siglent() {
		return new SiglentDeviceFactory();
	}

	@Bean
	public SCPIDeviceFactory deviceFactory(final List<ISCPIDeviceFactory> factories) {
		return new SCPIDeviceFactory(factories);
	}
}