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
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * Voice implementation that uses a WavePlayer.
 */
public class WaveVoice implements Voice {
	private WavePlayer output;

	/**
	 * Constructor.
	 * @param ac the AudioContext
	 * @param buffer the waveform
	 * @param freq frequency controller UGen
	 */
	public WaveVoice(AudioContext ac, Buffer buffer, UGen freq) {
		this.output = new WavePlayer(ac, freq, buffer);
	}

	@Override
	public UGen getOutput() {
		return output;
	}

	@Override
	public void noteOn(ShortMessage smsg, int note) {
		// Nothing to do
	}

	@Override
	public void noteOff(ShortMessage smsg, int note) {
		// Nothing to do
	}
	
}
