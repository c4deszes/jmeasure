package org.jmeasure.core.instrument;

import org.jmeasure.core.signal.digital.BinarySignal;
import org.jmeasure.core.util.EnumParameters;

import java.io.IOException;
import java.util.Optional;
/**
 * A logic analyzer is a device capable of making binary signal measurements over time
 * 
 * @author Balazs Eszes
 */
public interface LogicAnalyzer extends TimeDomainAnalyzer, AutoCloseable {

	public final static float TTL = 1.5f;

	public final static float CMOS = 1.65f;

	public final static float LVCMOS3V3 = 1.65f;

	public final static float LVCMOS2V5 = 1.25f;

	int getDigitalInputCount();

	void setDigitalInputEnabled(int channel, boolean enabled);

	void setDigitalInputThreshold(int channel, float threshold);
	
	Optional<BinarySignal> getDigitalInputSignal(int channel) throws IOException;
}