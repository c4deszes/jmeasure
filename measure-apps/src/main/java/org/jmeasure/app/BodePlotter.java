package org.jmeasure.app;

/**
 * BodePlotter
 */
public class BodePlotter {

	float amplitude;

	float offset;

	float load;

	float startFrequency;

	float stopFrequency;

	int points;

	BodePlotter start(double start) {
		return this;
	}

	BodePlotter stop(double stop) {
		return this;
	}

	BodePlotter range(double center, double span) {

		return this;
	}

}