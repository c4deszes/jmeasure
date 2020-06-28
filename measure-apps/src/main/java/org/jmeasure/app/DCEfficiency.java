package org.jmeasure.app;

import org.jmeasure.core.instrument.DCLoad;
import org.jmeasure.core.instrument.DCPowerSupply;

/**
 * DCEfficiency
 */
public class DCEfficiency {

	DCPowerSupply psu;

	DCLoad load;

	float setVoltage;

	float startCurrent;

	float endCurrent;

	int points;
	
}