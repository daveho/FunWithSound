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
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Reverb;

public class AddReverb implements AddEffect {
	/**
	 * Reverb parameters.
	 */
	public static class Params {
		public double damping;
		public double earlyReflectionsLevel;
		public double lateReverbLevel;
		public double roomSize;
		
		public Params(double damping, double earlyReflectionsLevel, double lateReverbLevel, double roomSize) {
			this.damping = damping;
			this.earlyReflectionsLevel = earlyReflectionsLevel;
			this.lateReverbLevel = lateReverbLevel;
			this.roomSize = roomSize;
		}
	}
	
	/**
	 * Get default reverb parameters.
	 * 
	 * @return default reverb parameters
	 */
	public static Params defaultParams() {
		// These are the Beads defaults
		return new Params(0.7, 1.0, 1.0, 0.5);
	}
	
	private Params params;
	
	/**
	 * Constructor: adds reverb with default parameters.
	 */
	public AddReverb() {
		this.params = null;
	}
	
	/**
	 * Constructor: add reverb with specified parameters.
	 * 
	 * @param params the reverb parameters
	 */
	public AddReverb(Params params) {
		this.params = params;
	}
	
	@Override
	public UGen apply(AudioContext ac, InstrumentInfo info) {
		Reverb reverb = new Reverb(ac, 2);
		reverb.addInput(info.tail);
		
		if (params != null) {
			reverb.setDamping((float)params.damping);
			reverb.setEarlyReflectionsLevel((float)params.earlyReflectionsLevel);
			reverb.setLateReverbLevel((float)params.lateReverbLevel);
			reverb.setSize((float)params.roomSize);
		}

		// A Gain is used to mix the reverb output with the original signal
		Gain mix = new Gain(ac, 2);
		mix.addInput(reverb);
		mix.addInput(info.tail);
		
		// The Gain is now at the end of the effects chain
		return mix;
	}
}
