package org.jmeasure.core.instrument;

import org.jmeasure.core.util.EnumParameters;

import java.io.IOException;

/**
 * TimeDomainAnalyzer is any device capable of time correlated measurements
 * 
 * @author Balazs Eszes
 */
interface TimeDomainAnalyzer {

	/**
	 * Sets the timebase, including the trigger offset and seconds per division settings
	 * 
	 * <p>Note: since each scope will have different time divisions the user will instead use the Span value
	 * to determine how wide of a time range he wants to look at. 
	 * 
	 * <p>The driver can round the span value to whatever is available, but should always round to a higher value
	 * than specified
	 * 
	 * @param timebase Timebase setting
	 */
	void setTimebase(TimebaseSettings timebase) throws IOException;

	/**
	 * Sets trigger state, like Auto, Normal and Single shot capture
	 * 
	 * @param state Trigger state
	 */
	void setTriggerState(Trigger.State state) throws IOException;

	public static class Trigger {
	
		public static class Settings {
			int source;
	
			Type type;
	
	
		}
	
		public static enum Type {
			EDGE, SLEW, DROPOUT, INTERVAL, PULSE
		}
	
		public static enum State {
			AUTO, NORMAL, SINGLE, STOP
		}
	
	}

	public static class TimebaseSettings extends EnumParameters<TimebaseParameter> {
		
		private TimebaseSettings() {

		}

		public static TimebaseSettings builder() {
			return new TimebaseSettings();
		}

		public TimebaseSettings offset(float seconds) {
			this.put(TimebaseParameter.OFFSET, seconds);
			return this;
		}

		public TimebaseSettings span(float seconds) {
			this.put(TimebaseParameter.SPAN, seconds);
			return this;
		}

		public TimebaseSettings span(float secPerDiv, int divs) {
			this.put(TimebaseParameter.SPAN, secPerDiv * divs);
			return this;
		}
		
	}

	public static enum TimebaseParameter {
		OFFSET, SPAN
	}
}