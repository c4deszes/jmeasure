# SCPI Device implementation

## Introduction

A SCPI device is able to respond to standard SCPI commands

## Library guideline

1. The artifactId should be 'measure-driver-vendorName', e.g: 'measure-driver-keysight', 'measure-driver-tektronix'

## Device guideline

1. All SCPI devices should inherit the SCPIDevice class

2. Devices should implement interfaces found inside `org.jmeasure.core.device`, for example a Mixed Signal Oscilloscope (MSO) would implement Oscilloscope and LogicAnalyzer interfaces.

3. Devices should have functionality not covered by the interfaces as public methods

4. For every vendor there should be at least one factory class

5. Usually there are different variants of a product, it's recommended to implement them all using a single class. This generic device should have the model's name/number replaced with X's or 0's accordingly, while still being clear that it's the common driver for multiple devices.

## Example

```java
class org.jmeasure.keysight.Arb33600 extends SCPIDevice implements WaveformGenerator {

    public Arb33600(SCPISocket socket, DeviceIdentifier idn) {
        super(socket, idn);
    }

    @Override
    public void setOutputConfiguration(int channel, OutputConfiguration output) {
        boolean enabled = output.getBoolean(OutputParameter.ENABLED);
        this.send(SCPICommand.builder().command("OUTP" + channel).with(enabled ? "ON": "OFF").build());
        //...
    }

    @Override
    public void setAnalogWaveform(int channel, Waveform waveform) {
        this.send(SCPICommand.builder().command("SOUR" + channel, "FUNC").with(waveform.getType().toString()).build());
        //...
    }

    //device specific functionality
    public void setChannelCoupling(CouplingMode mode, boolean state) {
        //..
    }

    //...
}
```

```java
class KeysightDeviceFactory implements ISCPIDeviceFactory {

    boolean supports(DeviceIdentifier idn) {
        return idn.getManufacturer().equals("Keysight Technologies");
    }

    SCPIDevice create(DeviceIdentifier idn, SCPISocket socket) {
        if(idn.getModel().matches("336[0-9]{2}[AB]{1}")) {
            return new Arb33600(socket, idn);
        }
        throw new UnsupportedDeviceException();
    }

}
```
