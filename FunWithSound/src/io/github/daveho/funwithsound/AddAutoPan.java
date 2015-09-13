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
import net.beadsproject.beads.ugens.Panner;

/**
 * Add an auto-pan effect, where a panner is fed by a sine
 * wave oscillating at a specified frequency within a
 * specified range.
 */
public class AddAutoPan implements AddEffect {
	public double freqHz;
	public double min;
	public double max;

	/**
	 * Constructor.
	 * 
	 * @param freqHz oscillation frequency
	 * @param min minimum pan value
	 * @param max maximum pan value
	 */
	public AddAutoPan(double freqHz, double min, double max) {
		this.freqHz = freqHz;
		this.min = min;
		this.max = max;
	}

	@Override
	public UGen apply(AudioContext ac, InstrumentInfo info) {
		// Constrain the sine wave to be between min and max
		UGen limitedSine = Util.getRangedSineFunction(ac, min, max, freqHz);
		
		Panner panner = new Panner(ac);
		panner.setPos(limitedSine);
		
		panner.addInput(info.tail);
		
		return panner;
	}

}
