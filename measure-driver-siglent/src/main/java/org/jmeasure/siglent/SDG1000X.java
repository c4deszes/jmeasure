package org.jmeasure.siglent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jmeasure.core.device.WaveformGenerator;
import org.jmeasure.core.device.configuration.ClockSource;
import org.jmeasure.core.signal.Waveform;
import org.jmeasure.core.signal.Waveform.WaveformParameter;
import org.jmeasure.core.signal.Waveform.WaveformType;
import org.jmeasure.core.signal.analog.AnalogSignal;
import org.jmeasure.core.signal.Signal;
import org.jmeasure.core.signal.sampler.EdgeSampler;
import org.jmeasure.lxi.DeviceIdentifier;
import org.jmeasure.lxi.SCPICommand;
import org.jmeasure.lxi.SCPIDevice;
import org.jmeasure.lxi.SCPISocket;

/**
 * SDG1000X
 */
public class SDG1000X extends SCPIDevice implements WaveformGenerator {

	private final static Map<WaveformType, String> WF_TYPEMAP = new HashMap<>();

	private final static Map<WaveformParameter, String> WF_PARAM_TYPEMAP = new HashMap<>();

	static {
		WF_TYPEMAP.put(WaveformType.SINE, "SINE");
		WF_TYPEMAP.put(WaveformType.SQUARE, "SQUARE");
		WF_TYPEMAP.put(WaveformType.RAMP, "RAMP");
		WF_TYPEMAP.put(WaveformType.PULSE, "PULSE");
		WF_TYPEMAP.put(WaveformType.NOISE, "NOISE");
		WF_TYPEMAP.put(WaveformType.DC, "DC");

		WF_PARAM_TYPEMAP.put(WaveformParameter.AMPLITUDE, "AMP");
		WF_PARAM_TYPEMAP.put(WaveformParameter.FREQUENCY, "FRQ");
		WF_PARAM_TYPEMAP.put(WaveformParameter.PHASE, "PHSE");
		WF_PARAM_TYPEMAP.put(WaveformParameter.OFFSET, "OFST");
		WF_PARAM_TYPEMAP.put(WaveformParameter.SYMMETRY, "SYM");

		WF_PARAM_TYPEMAP.put(WaveformParameter.DUTY, "DUTY");
		WF_PARAM_TYPEMAP.put(WaveformParameter.WIDTH, "WIDTH");
		WF_PARAM_TYPEMAP.put(WaveformParameter.DELAY, "DLY");
		WF_PARAM_TYPEMAP.put(WaveformParameter.FALL, "FALL");
		WF_PARAM_TYPEMAP.put(WaveformParameter.RISE, "RISE");

		WF_PARAM_TYPEMAP.put(WaveformParameter.STDEV, "STDEV");
		WF_PARAM_TYPEMAP.put(WaveformParameter.MEAN, "MEAN");
	}

	public final static long DEFAULT_TIMEOUT = 1000;

	public final static int WAVE_LENGTH = 16384;

	public final static int VERTICAL_RESOLUTION = 65535;

	private EdgeSampler<Float> sampler = new EdgeSampler<Float>(WAVE_LENGTH);

	public SDG1000X(DeviceIdentifier deviceIdentifier, SCPISocket socket) throws IOException {
		super(deviceIdentifier, socket);
	}

	@Override
	public synchronized void setOutputConfiguration(int channel, OutputConfiguration output) throws IOException {
		validateChannelNumber(channel);

		SCPICommand.Builder builder = channelCommand(channel, "OUTP");
		output.<Boolean>get(OutputParameter.ENABLED).ifPresent(enabled -> builder.with(enabled ? "ON" : "OFF"));
		output.<Float>get(OutputParameter.LOAD).ifPresent(load -> {
			validateOutputImpedance(load);
			builder.with("LOAD", load == WaveformGenerator.HIZ ? "HZ" : String.valueOf(load));
		});
		output.<Boolean>get(OutputParameter.INVERTED)
				.ifPresent(inverted -> builder.with("PLRT", inverted ? "INVT" : "NOR"));

		this.send(builder.build());
		this.waitForOPC(DEFAULT_TIMEOUT);
	}

	@Override
	public synchronized void setAnalogSignal(int channel, Signal<Float> signal) throws IOException {
		validateChannelNumber(channel);
		if (signal.getData().size() != WAVE_LENGTH) {
			signal = sampler.sample(signal);
		}

		SCPICommand arbMode = channelCommand(channel, "BSWV").with("WVTP", "ARB").build();
		this.send(arbMode);
		waitForOPC(DEFAULT_TIMEOUT);

		SCPICommand srate = channelCommand(channel, "SRATE").with("MODE", "TARB").build();
		this.send(srate);
		waitForOPC(DEFAULT_TIMEOUT);

		SCPICommand wdata = channelCommand(channel, "WVDT").with("WVNM", signal.getId()).with("LENGTH", "32KB")
				.with("FREQ", 1.0f / signal.period()).with("AMPL", signal.max() - signal.min())
				.with("OFST", (signal.max() + signal.min()) / 2.0f)
				.with("WAVEDATA", new String(binary(signal).array(), StandardCharsets.ISO_8859_1)).build();
		this.send(wdata);
		waitForOPC(DEFAULT_TIMEOUT);

		SCPICommand arbWave = channelCommand(channel, "ARWV").with("NAME", signal.getId()).build();
		this.send(arbWave);
		waitForOPC(DEFAULT_TIMEOUT);
	}

	private static ByteBuffer binary(Signal<Float> signal) {
		ByteBuffer buffer = ByteBuffer.allocate(16384 * 2);
		double min = signal.min();
		double max = signal.max();
		signal.getData().forEach(point -> {
			double k = (point.value - min) * 2 / (max - min) - 1;
			short value = (short) (k * 65535 / 2);
			byte low = (byte) (value & 0xFF);
			byte high = (byte) ((value & 0xFF00) >> 8);
			buffer.put(low);
			buffer.put(high);
		});
		return buffer;
	}

	@Override
	public synchronized void setAnalogWaveform(int channel, Waveform waveform) throws IOException {
		validateChannelNumber(channel);
		validateWaveform(waveform);

		SCPICommand.Builder commandBuilder = channelCommand(channel, "BSWV").with("WVTP",
				WF_TYPEMAP.get(waveform.getType()));
		for (WaveformParameter param : waveform.getParameters()) {
			commandBuilder.with(WF_PARAM_TYPEMAP.get(param), waveform.get(param).toString());
		}

		this.send(commandBuilder.build());
		waitForOPC(DEFAULT_TIMEOUT);
	}

	public void validateWaveform(Waveform waveform) {
		// if(waveform.has(WaveformParameter.FREQUENCY) &&
		// waveform.assertAsFloat(WaveformParameter., predicate))
		// if(waveform.getType() == WaveformType.SINE) {

		// }
	}

	public void validateOutputImpedance(float load) {
		if (load < 50 || (load > 10000 && load != WaveformGenerator.HIZ)) {
			throw new IllegalArgumentException("Output impedance must be 50-10000 Ohms or HiZ");
		}
	}

	public void validateChannelNumber(int channel) {
		if (channel != 1 && channel != 2) {
			throw new IllegalArgumentException("Channel must be 1 or 2");
		}
	}

	private static SCPICommand.Builder channelCommand(int channel, String command) {
		return SCPICommand.builder().command("C" + channel, command);
	}

	@Override
	public int getAnalogOutputCount() {
		return 2;
	}

	@Override
	public void setClockSource(ClockSource source) throws IOException {

	}

	@Override
	public Optional<AnalogSignal> getAnalogSignal(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Waveform> getAnalogWaveform(int channel) {
		// TODO Auto-generated method stub
		return null;
	}
	
}