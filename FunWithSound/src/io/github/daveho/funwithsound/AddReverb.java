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
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Reverb;

/**
 * Add a reverb effect.
 * Accepts parameter configuration via a DataBead.
 * Note that parameters can only be set at effect creation time,
 * not during runtime.
 * The parameter DataBead uses the same property names
 * as the Reverb UGen.
 */
public class AddReverb implements ParamNames, AddEffect {
	private DataBead params;
	
	/**
	 * Constructor: adds reverb with default parameters.
	 */
	public AddReverb() {
		this.params = Defaults.reverbDefaults();
	}
	
	/**
	 * Constructor: add reverb with specified parameters.
	 * 
	 * @param params the reverb parameters
	 */
	public AddReverb(DataBead params) {
		this.params = params;
	}
	
	@Override
	public UGen apply(AudioContext ac, RealizedInstrument info) {
		Reverb reverb = new Reverb(ac, 2);
		reverb.addInput(info.tail);

		reverb.sendData(params);

		// A Gain is used to mix the reverb output with the original signal
		Gain mix = new Gain(ac, 2);
		mix.addInput(reverb);
		mix.addInput(info.tail);
		
		// The Gain is now at the end of the effects chain
		return mix;
	}
}
