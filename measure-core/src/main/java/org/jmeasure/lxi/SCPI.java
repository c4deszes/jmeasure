package org.jmeasure.lxi;

/**
 * Includes standard SCPI commands, such as resetting the device, waiting for an operation
 */
public final class SCPI {

	public final static SCPICommand idnQuery = SCPICommand.builder().query("*IDN").build();

	public final static SCPICommand opcQuery = SCPICommand.builder().query("*OPC").build();

	public final static SCPICommand resetDevice = SCPICommand.builder().command("*RST").build();

	public final static SCPICommand testDevice = SCPICommand.builder().query("*TST").build();
}