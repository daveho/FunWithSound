// FunWithSound - A Java/Processing library for music composition
// Copyright 2015, David Hovemeyer <david.hovemeyer@gmail.com>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package io.github.daveho.funwithsound;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.CombFilter;

/**
 * Add a flanger effect.
 * Accepts parameter configuration via a DataBead.
 * Note that parameters can only be set at effect creation time,
 * not during runtime.
 */
public class AddFlanger implements AddEffect {
	// default parameter values as constants
	public static final double DEFAULT_H = .3;
	public static final double DEFAULT_G = .8;
	public static final double DEFAULT_A = .8;
	public static final double DEFAULT_MAX_DELAY_MS = 10.0;
	public static final double DEFAULT_MIN_DELAY_MS = 5.0;
	public static final double DEFAULT_FREQ_HZ = 1.0;
	
	// Property names
	
	/** DataBead property name: Frequency (rate at which the flanger's delay changes). */
	public static final String FREQ_HZ = "freqHz";
	/** DataBead property name: Minimum delay in milliseconds. */
	public static final String MIN_DELAY_MS = "minDelayMs";
	/** DataBead property name: Maximum delay in milliseconds. */
	public static final String MAX_DELAY_MS = "maxDelayMs";
	/** DataBead property name: Gain for original signal through the flanger's comb filter. */
	public static final String A = "a";
	/** DataBead property name: Gain for the delayed signal through the flanger's comb filter. */
	public static final String G = "g";
	/** DataBead property name: Gain for the feed-forward component of the flanger's comb filter. */
	public static final String H = "h";
	
	/**
	 * Create default parameters.
	 * They are completely arbitrary, but
	 * can serve as a useful starting point.
	 */
	public static final DataBead defaultParams() {
		return new DataBead(
				FREQ_HZ, DEFAULT_FREQ_HZ,
				MIN_DELAY_MS, DEFAULT_MIN_DELAY_MS,
				MAX_DELAY_MS, DEFAULT_MAX_DELAY_MS,
				A, DEFAULT_A,
				G, DEFAULT_G,
				H, DEFAULT_H
				);
	}

	private DataBead params;
	
	/**
	 * Constructor.
	 * The default parameters will be used.
	 */
	public AddFlanger() {
		this.params = defaultParams();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param params a DataBead containing the parameters
	 */
	public AddFlanger(DataBead params) {
		this.params = params;
	}

	@Override
	public UGen apply(AudioContext ac, RealizedInstrument info) {
		final int mindel = (int) ac.msToSamples(Util.getDouble(params, MIN_DELAY_MS));
		final int maxdel = (int) Math.ceil(ac.msToSamples(Util.getDouble(params, MAX_DELAY_MS)));
		
		CombFilter comb = new CombFilter(ac, maxdel);
		
		// Modulate the comb filter's delay with a sine function
		UGen delay = Util.getRangedSineFunction(ac, mindel, maxdel, Util.getDouble(params, FREQ_HZ));
	
		comb.setDelay(delay);
		comb.setA(Util.getFloat(params, A));
		comb.setG(Util.getFloat(params, G));
		comb.setH(Util.getFloat(params, H));
		
		comb.addInput(info.tail);
		
		return comb;
	}

}
