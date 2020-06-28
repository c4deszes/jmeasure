package org.jmeasure.cli.scpi;

import java.io.File;
import java.util.List;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;

public class SCPISend {
	
	@ArgGroup(exclusive = true)
	Input input;
	
	class Input {
		@Option(names = {"--commands", "-c"}, required = false)
		List<String> commands;

		@Option(names = {"--script"}, required = false)
		File script;

		@Option(names = {"--interactive", "-i"}, required = true)
		boolean interactive;
	}
}