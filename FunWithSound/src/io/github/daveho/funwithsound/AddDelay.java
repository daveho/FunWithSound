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
 * Add a delay effect.
 */
public class AddDelay implements AddEffect {
	private double delayMs;
	private double passThruGain;
	private double delayGain;

	/**
	 * Constructor.
	 * 
	 * @param delayMs       the delay in milliseconds
	 * @param passThruGain  the gain for the original signal
	 * @param delayGain     the gain for the delayed signal
	 */
	public AddDelay(double delayMs, double passThruGain, double delayGain) {
		this.delayMs = delayMs;
		this.passThruGain = passThruGain;
		this.delayGain = delayGain;
	}

	@Override
	public UGen apply(AudioContext ac, RealizedInstrument info) {
		int delaySamples = (int) Math.ceil(ac.msToSamples(delayMs));
		
		CombFilter comb = new CombFilter(ac, delaySamples);
		
		comb.setDelay(delaySamples);
		comb.setA((float)passThruGain);
		comb.setG((float)delayGain);
		
		comb.addInput(info.tail);
		
		return comb;
	}

}
