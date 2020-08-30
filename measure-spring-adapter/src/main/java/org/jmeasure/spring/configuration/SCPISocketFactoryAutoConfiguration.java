package org.jmeasure.spring.configuration;

import java.util.List;
import java.util.Set;

import org.jmeasure.core.scpi.mock.MockSCPIClass;
import org.jmeasure.core.scpi.mock.MockSCPISocket;
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

	/*
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
				//TODO: fix
				//log.debug("Registered MockSCPISocket: name=" + name + ", class=" + klass.getName());
			} catch(Exception e) {
				//log.warn("Failed to register " + def.getBeanClassName(), e);
			}
		});
		return factory;
	}
	*/
}