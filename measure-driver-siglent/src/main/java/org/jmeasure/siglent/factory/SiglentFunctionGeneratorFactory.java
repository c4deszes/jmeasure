package org.jmeasure.siglent.factory;

public class SiglentFunctionGeneratorFactory {
	public final static Pattern BENCHTOP_FGEN_REGEX = Pattern.compile("SDG(?<series-prefix>[123456])(?<bandwidth>[0-9]{2})(?<channels>[24])(?<series-postfix>X?)");
}