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
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;

/**
 * Generic attack/sustain/release note envelope.
 */
public class ASRNoteEnvelope implements NoteEnvelope {
	private DataBead params;
	private Envelope envelope;
	private Gain gain;

	/**
	 * Constructor.
	 * @param ac the AudioContext
	 * @param params parameters
	 * @param input input UGen
	 */
	public ASRNoteEnvelope(AudioContext ac, DataBead params, UGen input) {
		this.params = params;
		this.envelope = new Envelope(ac);
		this.envelope.setValue(0.0f);
		this.gain = new Gain(ac, 2, envelope);
		this.gain.addInput(input);
	}
	
	@Override
	public UGen getOutput() {
		return gain;
	}

	@Override
	public void noteOn(ShortMessage smsg, int note) {
//		System.out.printf("Env note on note=%d\n", note);
		envelope.clear();
		int velocity = smsg.getData2();
		float minGain = Util.getFloat(params, ParamNames.MIN_GAIN);
		float gain = minGain + ((1.0f - minGain) * (velocity/127.0f));
		float attackTimeMs = Util.getFloat(params, ParamNames.ATTACK_TIME_MS);
//		System.out.printf("Ramp to gain %f in %f ms\n", gain, attackTimeMs);
		envelope.addSegment(gain, attackTimeMs);
	}

	@Override
	public void noteOff(ShortMessage smsg, int note) {
//		System.out.printf("Env note off note=%d\n", note);
		envelope.addSegment(0.0f, Util.getFloat(params, ParamNames.RELEASE_TIME_MS));
	}
}