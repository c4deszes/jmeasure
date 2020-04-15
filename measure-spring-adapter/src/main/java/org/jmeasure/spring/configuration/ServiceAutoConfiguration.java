package org.jmeasure.spring.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jmeasure.core.scpi.SCPIService;
import org.jmeasure.core.scpi.factory.SCPIDeviceFactory;
import org.jmeasure.core.scpi.factory.SCPISocketFactory;
import org.jmeasure.core.visa.VISAResourceManager;
import org.jmeasure.core.visa.VisaDeviceFactory;
import org.jmeasure.core.visa.factory.SocketFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ServiceAutoConfiguration
 */
@Configuration
public class ServiceAutoConfiguration {

    @Bean
    public SCPIService scpiService(SCPISocketFactory scpiSocketFactory, SCPIDeviceFactory scpiDeviceFactory) {
        return new SCPIService(scpiSocketFactory, scpiDeviceFactory);
    }

    @Bean
    public VISAResourceManager visaResourceManager(SocketFactory socketFactory, List<VisaDeviceFactory<?>> visaFactories) {
        return new VISAResourceManager(socketFactory, visaFactories, Collections.emptyList());
    }
    
}