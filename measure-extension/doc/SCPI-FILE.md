# SCPI File specification

## File extension

SCPI script files shall use the `.scpi` file extension.

## Comments

Lines starting with \# are ignored, as well as characters following \# until new line.

```scpi
#this is a comment
SYS:RST #this is a comment as well
```

### Extensions

Comment in the first line will be interpreted as the runner type if it's in the `#!<runner type>` format.

Script runners shall at minimum support `#!/runner/basic` runner, which disables language extensions. In basic mode the runner must support Comments, Commands and Queries.

If the specified type is unsupported by the runner then it should exit without running the script.

---

## Commands

Every line is interpreted as a command to be sent to the instrument.

Commands may be broken into multiple lines via `\` before the new line.

```scpi
C1:OUTPUT \
    VOLTS,5.0,\
    CURRENT,1.0
C1:OUTPUT:ENABLE
```

Multiple commands can be sent in a single step, the commands must be separated via `;` character.

```scpi
C1:OUTPUT VOLTS,5.0; C1:OUTPUT:ENABLE; C1:OUTPUT? CURRENT
```

Script runners shall be able to execute the correct commands. By default the receive buffer shall be flushed after every command.

---

## Queries

Commands ending with `?` are considered queries. When a query is sent the runner will wait for a response

```scpi
C1:OUTPUT:CURRENT?
```

Script runners shall read the reply of a query and be able to write it to standard output or into a file. Write mode may be Override, Append, Append (without newline)

---

## Parameters

Scripts can be parameterized via `@` blocks.

### Via arguments

Script runners shall expand arguments passed via user interface into `@arg('name')` blocks.

For example a command line based runner shall

### Via files

Script runners shall read files and expand them into `@file('path')` blocks.

For example the following command

---

## Special functions

### Delay

Script runners shall insert delays `+delay('time')`

Valid time formats

+ 100ns

+ 100us

+ 100ms

+ 100s

+ 100m

+ 100h

---
