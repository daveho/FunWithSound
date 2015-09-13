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
import net.beadsproject.beads.ugens.CombFilter;

/**
 * Add a flanger effect.
 */
public class AddFlanger implements AddEffect {
	public static final double DEFAULT_H = .3;
	public static final double DEFAULT_G = .8;
	public static final double DEFAULT_A = .8;
	public static final double DEFAULT_MAX_DELAY_MS = 10.0;
	public static final double DEFAULT_MIN_DELAY_MS = 5.0;
	public static final double DEFAULT_FREQ_HZ = 1.0;

	/**
	 * Flanger parameters.
	 */
	public static class Params {
		/** Frequency (rate at which the flanger's delay changes). */
		public double freqHz;
		/** Minimum delay in milliseconds. */
		public double minDelayMs;
		/** Maximum delay in milliseconds. */
		public double maxDelayMs;
		/** Gain for original signal through the flanger's comb filter. */
		public double a;
		/** Gain for the delayed signal through the flanger's comb filter. */
		public double g;
		/** Gain for the feed-forward component of the flanger's comb filter. */
		public double h;
		
		public Params(double freqHz, double minDelayMs, double maxDelayMs, double a, double g, double h) {
			this.freqHz = freqHz;
			this.minDelayMs = minDelayMs;
			this.maxDelayMs = maxDelayMs;
			this.a = a;
			this.g = g;
			this.h = h;
		}
	}
	
	/**
	 * Create default parameters.
	 * They are completely arbitrary, but
	 * can serve as a useful starting point.
	 */
	public static final Params defaultParams() {
		return new Params(
			DEFAULT_FREQ_HZ,
			DEFAULT_MIN_DELAY_MS,
			DEFAULT_MAX_DELAY_MS,
			DEFAULT_A,
			DEFAULT_G,
			DEFAULT_H
		);
	}
	
	private Params params;
	
	public AddFlanger(Params params) {
		this.params = params;
	}

	@Override
	public UGen apply(AudioContext ac, InstrumentInfo info) {
		final int mindel = (int) ac.msToSamples(params.minDelayMs);
		final int maxdel = (int) Math.ceil(ac.msToSamples(params.maxDelayMs));
		
		CombFilter comb = new CombFilter(ac, maxdel);
		
		// Modulate the comb filter's delay with a sine function
		UGen delay = Util.getRangedSineFunction(ac, mindel, maxdel, params.freqHz);
	
		comb.setDelay(delay);
		comb.setA((float)params.a);
		comb.setG((float)params.g);
		comb.setH((float)params.h);
		
		comb.addInput(info.tail);
		
		return comb;
	}

}
