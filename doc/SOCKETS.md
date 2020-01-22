# SCPI Socket implementation

## Introduction

A SCPI device usually has more than one communication interface, it could be USB, GPIB or LAN. To support all of these without having to write drivers for all device - interface combinations the core library includes a common interface for all SCPI communications.

```java
interface SCPISocket {

    void send(SCPICommand... commands) throws IOException;

    Optional<SCPICommand> receive(long timeout) throws IOException;

    void connect() throws IOException;

    void disconnect();

    boolean isConnected();
}
```

## Guideline

### Connection

1. `connect()` shouldn't be called inside the contructor of a socket, rather inside the driver/device class

2. `connect()` and `disconnect()` should be **idempotent**, meaning that consecutive calls shouldn't alter the connection's state

### Transmission

1. `send(SCPICommand...)` should be **atomic** but only to the extent of what the socket can assume in it's given context.
For example `send("SETV 5.0", "SETI 1.0")` should only update the voltage value if it's also able to set the current value. Since this is heavily device dependent it's mostly up to the driver to solve these problems, but the socket should provide the best implementation for atomic operations.

2. `send(SCPICommand...)` **has to guarantee sequential operation** if it's only given a single command.

3. `send(SCPICommand...)` **doesn't guarantee sequential operation** when sending multiple commands, it may send some of them overlapped and sequentially. The driver must be aware of this.

### Reception

1. `receive(long timeout)` should wait indefinitely if `timeout == 0`

2. `receive(long timeout)` should return 
