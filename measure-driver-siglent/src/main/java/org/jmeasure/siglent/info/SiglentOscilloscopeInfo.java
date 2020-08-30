package org.jmeasure.siglent.info;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SiglentOscilloscopeInfo {
	
	public final static Pattern SCOPE_REGEX = Pattern.compile("SDS(?<series-prefix>[123456])(?<bandwidth>[0-9]{2})(?<channels>[24])(?<series-postfix>DL\\+|CML\\+|CFL|X(\\+|-E)?)");

	private int series;

	private int channels;

	private String postfix;

	public static SiglentOscilloscopeInfo create(String model) throws IllegalArgumentException {
		Matcher matcher = SCOPE_REGEX.matcher(model);
		if(!matcher.matches()) {
			throw new IllegalArgumentException();
		}
		int series = Integer.parseInt(matcher.group("series-prefix"));
		int channels = Integer.parseInt(matcher.group("channels"));
		String postfix = matcher.group("series-postfix");
		return new SiglentOscilloscopeInfo(series, channels, postfix);
	}

	/**
	 * @param series
	 * @param channels
	 * @param postfix
	 */
	public SiglentOscilloscopeInfo(int series, int channels, String postfix) {
		this.series = series;
		this.channels = channels;
		this.postfix = postfix;
	}

	/**
	 * @return the scopeRegex
	 */
	public static Pattern getScopeRegex() {
		return SCOPE_REGEX;
	}

	/**
	 * @return the series
	 */
	public int getSeries() {
		return series;
	}

	/**
	 * @return the channels
	 */
	public int getChannels() {
		return channels;
	}

	/**
	 * @return the postfix
	 */
	public String getPostfix() {
		return postfix;
	}
	
}