# Device implementation

## Introduction

A SCPI device is able to respond to standard SCPI commands

## Library guideline

1. The artifactId should be 'measure-driver-vendorName', e.g: 'measure-driver-keysight', 'measure-driver-tektronix'

## Device guideline

1. Devices should implement interfaces found inside `org.jmeasure.core.instrument`, for example a Mixed Signal Oscilloscope (MSO) would implement Oscilloscope and LogicAnalyzer interfaces.

2. Devices should have functionality not covered by the interfaces as public methods

3. For every vendor there should be at least one factory class

4. Usually there are different variants of a product, it's recommended to implement them all using a single class. This generic device should have the model's name/number replaced with X's or 0's accordingly, while still being clear that it's the common driver for multiple devices. Then inside this class using the device identifier parameter you can differentiate the models.

## Example

```java
class org.jmeasure.keysight.Arb336XX extends SCPISocketAdapter implements WaveformGenerator {

    public Arb336XX(SCPISocket socket, DeviceIdentifier idn) {
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

    SCPIDevice create(SCPISocket socket, DeviceIdentifier idn) {
        if(idn.getModel().matches("336[0-9]{2}[AB]{1}")) {
            return new Arb336XX(socket, idn);
        }
        throw new UnsupportedDeviceException();
    }

}
```
