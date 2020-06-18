package org.jmeasure.siglent;

import java.io.IOException;
import java.util.Optional;

import org.jmeasure.core.instrument.Oscilloscope;
import org.jmeasure.core.instrument.TimeDomainAnalyzer;
import org.jmeasure.core.scpi.ISCPISocket;
import org.jmeasure.core.scpi.SCPICommand;
import org.jmeasure.core.scpi.SCPISocketAdapter;
import org.jmeasure.core.signal.analog.AnalogSignal;
import org.jmeasure.core.visa.DeviceIdentifier;

/**
 * SDS1000XE
 */
public class SDS1000XE extends SCPISocketAdapter implements Oscilloscope {

	private final static int HORIZONTAL_DIVISIONS = 10;

	public SDS1000XE(ISCPISocket socket, DeviceIdentifier deviceIdentifier) throws IOException {
		super(socket);
	}

	@Override
	public void setTimebase(TimebaseSettings timebase) throws IOException {
		timebase.<Float>get(TimebaseParameter.SPAN).ifPresent(span -> {
			this.send(SCPICommand.builder().command("TDIV").with(""));
		});
	}

	@Override
	public void setTriggerState(TimeDomainAnalyzer.Trigger.State state) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getAnalogInputCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAnalogInput(int channel, AnalogInputConfiguration configuration) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public AnalogInputConfiguration getAnalogInput(int channel) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<AnalogSignal> getAnalogSignal(int channel) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAnalogTrigger(Settings trigger) throws IOException {
		// TODO Auto-generated method stub

	}

	
}