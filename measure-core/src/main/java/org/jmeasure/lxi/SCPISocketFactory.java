package org.jmeasure.lxi;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jmeasure.lxi.SCPISocket;
import org.jmeasure.lxi.SCPISocketFactory;

/**
 * DeviceFactory
 */
public class SCPISocketFactory {

	private List<ISCPISocketFactory<?>> factories;

	public SCPISocketFactory(ISCPISocketFactory<?>... factories) {
		this.factories = new LinkedList<>(Arrays.asList(factories));
	}

	public void add(ISCPISocketFactory<?> factory) {
		this.factories.add(factory);
	}

	public void remove(ISCPISocketFactory<?> factory) {
		this.factories.remove(factory);
	}

	public boolean supports(String connectionInfo) {
		return this.factories.stream().anyMatch(factory -> factory.supports(connectionInfo));
	}

	public SCPISocket create(String connectionInfo) throws Exception {
		for(ISCPISocketFactory<?> factory : factories) {
			if(factory.supports(connectionInfo)) {
				return factory.create(connectionInfo);
			}
		}
		return null;
	}

	/**
	 * SCPISocketFactory
	 */
	public static interface ISCPISocketFactory<T extends SCPISocket> {

		boolean supports(String connectionInfo);

		T create(String connectionInfo) throws Exception;
	}
}