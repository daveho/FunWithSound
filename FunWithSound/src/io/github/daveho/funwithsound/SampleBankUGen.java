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

import io.github.daveho.gervill4beads.Midi;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.core.UGenChain;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;
import net.beadsproject.beads.ugens.SamplePlayer.LoopType;

/**
 * A UGen that plays samples in response to MIDI NOTE_ON events.
 * It expects to receive MidiMessages from a Bead implementing
 * the MidiMessageSource interface, such as
 * ReceivedMidiMessageSource.  Also note that this UGen does
 * no sequencing of its own, so the Bead providing the messages
 * should deliver them at the correct playback times.
 */
public class SampleBankUGen extends UGenChain {
	private AudioContext ac;
	private Map<Integer, Sample> sampleBank;
	private Map<Integer, SamplePlayer> samplePlayers;

	/**
	 * Constructor.
	 * 
	 * @param ac the AudioContext
	 */
	public SampleBankUGen(AudioContext ac) {
		super(ac, 0, 2);
		this.ac = ac;
		sampleBank = new HashMap<Integer, Sample>();
		samplePlayers = new HashMap<Integer, SamplePlayer>();
	}
	
	/**
	 * Add a Sample to be played for given MIDI note.
	 * 
	 * @param note the MIDI note
	 * @param sample the Sample
	 */
	public void addSample(int note, Sample sample) {
		sampleBank.put(note, sample);
	}
	
	/**
	 * Prepare to play. This must be called before the AudioContext is started.
	 */
	public void prepareToPlay() {
		if (!samplePlayers.isEmpty()) {
			// We will assume that this method has already been called, and so
			// doesn't need to be called again.
			return;
		}
		
		// Create a SamplePlayer for each sample
		for (Map.Entry<Integer, Sample> entry : sampleBank.entrySet()) {
			SamplePlayer player = new SamplePlayer(ac, 2);
			player.setSample(entry.getValue());
			player.setLoopType(LoopType.NO_LOOP_FORWARDS);
			player.pause(true);
			samplePlayers.put(entry.getKey(), player);
			System.out.printf("Added sample player for note %d\n", entry.getKey());
		}
		
		// All of the SamplePlayers feed into a Gain,
		// which mixes the input
		UGen mixer = new Gain(ac, 2);
		for (SamplePlayer player : samplePlayers.values()) {
			mixer.addInput(player);
		}
		
		// The Gain is the output of the UGen
		addToChainOutput(mixer);
	}

	@Override
	protected void messageReceived(Bead message) {
		if (Midi.hasMidiMessage(message)) {
			MidiMessage msg = Midi.getMidiMessage(message);
			if (msg instanceof ShortMessage) {
				// Note that NOTE_OFF messages are ignored.
				// NOTE_ON messages trigger the appropriate sample to be played.
				if (msg.getStatus() == ShortMessage.NOTE_ON) {
					//System.out.println("Sample note on?");
					// Find the appropriate SamplePlayer
					int note = ((ShortMessage)msg).getData1();
					SamplePlayer player = samplePlayers.get(note);
					if (player != null) {
						double time = ac.getTime();
						//System.out.printf("Play sample %d at %f ms\n", note, time);
						player.reset();
						player.start();
					}
				}
			}
		}
	}
}
