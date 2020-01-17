package org.jmeasure.lxi;

/**
 * SCPI
 */
public final class SCPI {

	public final static SCPICommand idnQuery = SCPICommand.builder().query("*IDN").build();

	public final static SCPICommand opcQuery = SCPICommand.builder().query("*OPC").build();
}