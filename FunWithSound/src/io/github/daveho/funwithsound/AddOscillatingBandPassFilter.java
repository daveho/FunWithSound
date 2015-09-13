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
import net.beadsproject.beads.ugens.BiquadFilter;

/**
 * Add an oscillating bandpass filter which ranges between
 * specified minimum and maximum center frequencies,
 * oscillating with specified oscillation frequency.
 * This produces a sort of undulating wah effect.
 * Note that this does seem to attenuate the volume quite
 * a bit.
 */
public class AddOscillatingBandPassFilter implements AddEffect {
	private double minFreqHz, maxFreqHz;
	private double oscFreqHz;
	
	/**
	 * Constructor.
	 * 
	 * @param minFreqHz  minimum center frequency
	 * @param maxFreqHz  maximum center frequency
	 * @param oscFreqHz  oscillation frequency
	 */
	public AddOscillatingBandPassFilter(double minFreqHz, double maxFreqHz, double oscFreqHz) {
		this.minFreqHz = minFreqHz;
		this.maxFreqHz = maxFreqHz;
		this.oscFreqHz = oscFreqHz;
	}

	@Override
	public UGen apply(AudioContext ac, InstrumentInfo info) {
		BiquadFilter filter = new BiquadFilter(ac, 2, BiquadFilter.Type.BP_SKIRT);
		
		UGen osc = Util.getRangedSineFunction(ac, minFreqHz, maxFreqHz, oscFreqHz);
		filter.setFrequency(osc);
		
		filter.addInput(info.tail);
		
		return filter;
	}
}
