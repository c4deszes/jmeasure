package org.jmeasure.spring.configuration;

import org.jmeasure.core.scpi.SCPIService;
import org.jmeasure.core.scpi.factory.SCPISocketFactory;
import org.jmeasure.core.visa.VISAResourceManager;
import org.jmeasure.core.visa.factory.SocketFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ServiceAutoConfiguration
 */
@Configuration
public class ServiceAutoConfiguration {

    @Bean
    public SCPIService scpiService(SCPISocketFactory scpiSocketFactory) {
        return new SCPIService(scpiSocketFactory);
    }

    @Bean
    public VISAResourceManager visaResourceManager(SocketFactory socketFactory, SCPIService scpiService) {
        return new VISAResourceManager(socketFactory, scpiService);
    }
    
}