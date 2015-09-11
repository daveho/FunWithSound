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

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import io.github.daveho.gervill4beads.Midi;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGenChain;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * This is basically an experiment to try to create a custom
 * MIDI mono synth using Beads, and use it to render
 * parts of a composition (as the runtime implementation of
 * a custom {@link Instrument}.)  It actually sounds pretty
 * decent!  Need to make attack/decay envelope and
 * portamento controllable. 
 */
public class MonoSynthUGen extends UGenChain {
	private static final float GLIDE_TIME_MS = 100;
	private static final float ATTACK_TIME_MS = 20;
	private static final float DECAY_TIME_MS = 200;
	private static final float MIN_GAIN = .3f;
	
	private Glide freq;
	private WavePlayer player;
	private Envelope gainEnv;
	private Gain gain;
	private int note;

	/**
	 * Constructor.
	 * 
	 * @param ac      the AudioContext
	 * @param buffer  the buffer type (sine, square, triangle, etc.)
	 */
	public MonoSynthUGen(AudioContext ac, Buffer buffer) {
		super(ac, 0, 2);
		
		freq = new Glide(ac);
		player = new WavePlayer(ac, freq, buffer);
		gainEnv = new Envelope(ac);
		gain = new Gain(ac, 2, gainEnv);
		gain.addInput(player);
		
		freq.setGlideTime(GLIDE_TIME_MS);

		addToChainOutput(gain);
	}
	
	@Override
	protected void messageReceived(Bead message) {
		if (Midi.hasMidiMessage(message)) {
			MidiMessage msg = Midi.getMidiMessage(message);
			
			if (msg instanceof ShortMessage) {
				ShortMessage smsg = (ShortMessage) msg;
				int note = smsg.getData1();
				
				if (smsg.getCommand() == ShortMessage.NOTE_ON) {
					gainEnv.clear();
					
					int velocity = smsg.getData2();
					float gain = MIN_GAIN + ((1.0f - MIN_GAIN) * velocity/127.0f);
					gainEnv.addSegment(gain, ATTACK_TIME_MS);
					freq.setValue(Pitch.mtof(note));
					this.note = note;
				} else if (smsg.getCommand() == ShortMessage.NOTE_OFF) {
					// Only requests to stop playing the current note will be honored
					if (note == this.note) {
						gainEnv.addSegment(0.0f, DECAY_TIME_MS);
					}
				}
			}
		}
	}
}
