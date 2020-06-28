package org.jmeasure.cli;

import org.jmeasure.cli.lxi.LXIDiscover;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
	subcommands = {
		LXIDiscover.class
	}
)
public class JMeasureCLI {

	public static void main(String[] args) {
		int exitCode = new CommandLine(new JMeasureCLI()).execute(args);
		System.exit(exitCode);
	}
}