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

import javax.sound.midi.ShortMessage;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.Static;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * Experiment to use frequency modulation in a monosynth UGen.
 * I honestly don't know what I'm doing, but it sounds interesting.
 */
public class FMMonoSynthUGen extends MonoSynthUGen {
	// frequency of modulation signal
	private Static modFreq;

	public FMMonoSynthUGen(AudioContext ac, Buffer buffer, DataBead params,
			double[] freqMult, double[] oscGains) {
		super(ac, buffer, params, freqMult, oscGains);
	}

	@Override
	protected UGen createOscillator(AudioContext ac, Buffer buffer, UGen freqUGen) {
		this.modFreq = new Static(ac, 0.0f); // ctor will set the actual frequency
//		UGen mod = Util.rangedSineFunction(ac, -1, 1, modFreq);
		UGen mod = new WavePlayer(ac, modFreq, Buffer.SAW);
//		UGen mod = Util.rangedOscillator(ac, -1, 1, modFreq, Buffer.SAW);
		
		UGen modulatedFrequency = new Function(freqUGen, mod) {
			@Override
			public float calculate() {
				return x[0] * x[1];
			}
		};
		
		return new WavePlayer(ac, modulatedFrequency, buffer);
	}
	
	@Override
	protected void onNoteOn(ShortMessage smsg, int note) {
		float nextModFreq = (float)(.5*Pitch.mtof(note));
		//System.out.printf("Setting mod freq=%f\n", nextModFreq);
		modFreq.setValue(nextModFreq);
		super.onNoteOn(smsg, note);
	}
}
