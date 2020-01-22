package org.jmeasure.lxi.factory;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jmeasure.lxi.SCPISocket;

/**
 * A SCPISocketFactory 
 */
public class CompositeSCPISocketFactory {

	private List<ISCPISocketFactory> factories;

	public CompositeSCPISocketFactory(ISCPISocketFactory... factories) {
		this(Arrays.asList(factories));
	}

	public CompositeSCPISocketFactory(List<ISCPISocketFactory> factories) {
		this.factories = new LinkedList<>(factories);
	}

	public SCPISocket create(String connectionInfo) throws IOException {
		for(ISCPISocketFactory factory : factories) {
			if(factory.supports(connectionInfo)) {
				return factory.create(connectionInfo);
			}
		}
		return null;
	}

}