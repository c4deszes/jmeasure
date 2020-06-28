# JMeasure

JMeasure is a Java API for Test & Measurement devices, intended to be used for test automation. It's purpose is to extend continuous integration workflows with hardware tests.

The core consists of abstract interfaces for devices, communication interfaces and helper methods. Interfaces are similiar to those specified in the IVIFoundation's [instrument class specifications](http://www.ivifoundation.org/specifications/)

It is extended by drivers and factory classes for different vendors.

Lastly it has a CLI and samples that should work given the right drivers. Samples include creating bode plots, measuring I-V curve.

## Usage

```xml
<dependency>
    <groupId>org.jmeasure</groupId>
    <artifactId>measure-parent</artifactId>
</dependency>
```

## Examples

```java
@Service
public class TestService implements Runnable {

    @Autowired
    private SCPISocketFactory socketFactory;

    @Autowired
    private SCPIDeviceFactory deviceFactory;

    @Override
    public void run() {
        SCPISocket socket = socketFactory.connect("TCP0::192.168.1.2::5025::SOCKET");
        SCPIDevice device = deviceFactory.create(socket);

        device.send(SCPI.resetDevice);
        device.send(SCPICommand.builder().command("C1:OUTP").with("ON").with("LOAD", 50).build());

        device.close();
    }
}
```

## CLI

The command-line interface can be used to test out some functionality, such as sending signals to a waveform generator or making measurements on an oscilloscope. It's also an easy way to communicate with any device.

```sh
shell:>scpi connect TCP::192.168.1.2:5025::SOCKET --alias wavegen
Connected SDG1032X via TCP::192.168.1.2::5025::SOCKET
shell:>scpi send wavegen "*IDN?"
Siglent Technologies,SDG1032X,SDG1XABC123456,1.01.01.33R1
shell:>scpi send wavegen "C1:OUTP ON" --silent
shell:>_
```

## Building

1. `./mvnw install
2. `./mvnw -B package`

## Contributing

