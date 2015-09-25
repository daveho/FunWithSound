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
import net.beadsproject.beads.ugens.Gain;

/**
 * Implementation of {@link NoteEnvelope} that just turns
 * the input audio on and off abruptly.
 */
public class OnOffNoteEnvelope implements NoteEnvelope {
	private Gain gain;
	
	/**
	 * Constructor.
	 * 
	 * @param ac the AudioContext
	 * @param input the input audio
	 */
	public OnOffNoteEnvelope(AudioContext ac, UGen input) {
		gain = new Gain(ac, 2);
		gain.setGain(0.0f);
		gain.addInput(input);
	}

	@Override
	public UGen getOutput() {
		return gain;
	}

	@Override
	public void noteOn(ShortMessage smsg, int note) {
		gain.setGain(1.0f);
	}

	@Override
	public void noteOff(ShortMessage smsg, int note) {
		gain.setGain(0.0f);
	}
}
