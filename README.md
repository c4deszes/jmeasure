# JMeasure

JMeasure is a Java API for Test & Measurement devices, intended to be used for test automation. It's purpose is to extend CI processes with hardware tests. Different scenarios can be set up, tested with the ability to create test reports.

The core consists of abstract interfaces for devices, communication interfaces and helper methods. Interfaces are similiar to those specified in the IVIFoundation's [instrument class specifications](http://www.ivifoundation.org/specifications/)

It is extended by drivers and factory classes for different vendors.

Lastly it has a CLI and samples that should work given the right drivers. Samples include creating bode plots, U/I diagrams.

## Usage

```xml
<dependency>
    <groupId>org.jmeasure</groupId>
    <artifactId>measure-parent</artifactId>
</dependency>
```

## Examples

```java
import
```

## CLI

The command-line interface can be used to test out some functionality, such as sending signals to a waveform generator or making measurements on an oscilloscope. It's also an easy way to communicate with any device.

```sh
shell:>scpi connect TCP::192.168.1.2:5025::SOCKET
Connected SDG1032X via TCP::192.168.1.2::5025::SOCKET --alias wavegen
shell:>scpi send wavegen "*IDN?"
Siglent Technologies,SDG1032X,SDG1XABC123456,1.01.01.33R1
shell:>scpi send wavegen "C1:OUTP ON" --silent
shell:>_
```

## Building
