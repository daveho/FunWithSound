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
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.Panner;
import net.beadsproject.beads.ugens.WavePlayer;

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
		final float range = (float)(max-min);
		
		WavePlayer sine = new WavePlayer(ac, (float)freqHz, Buffer.SINE);
		// Constrain the sine wave to be between min and max
		Function limitedSine = new Function(sine) {
			@Override
			public float calculate() {
				float coeff = (x[0] + 1.0f) / 2.0f;
				return (float)min + coeff*range;
			}
		};
		
		Panner panner = new Panner(ac);
		panner.setPos(limitedSine);
		
		panner.addInput(info.tail);
		
		return panner;
	}

}
