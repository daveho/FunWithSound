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
import net.beadsproject.beads.ugens.Envelope;
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
	
	private static class PlayerInfo {
		SamplePlayer player;
		Envelope env;
		Gain gain;
	}
	
	private AudioContext ac;
	private Map<Integer, Sample> sampleBank;
	private Map<Integer, PlayerInfo> samplePlayers;
	private Map<Integer, SampleRange> sampleRanges;

	/**
	 * Constructor.
	 * 
	 * @param ac the AudioContext
	 */
	public SampleBankUGen(AudioContext ac) {
		super(ac, 0, 2);
		this.ac = ac;
		sampleBank = new HashMap<Integer, Sample>();
		samplePlayers = new HashMap<Integer, PlayerInfo>();
		sampleRanges = new HashMap<Integer, SampleRange>();
	}
	
	/**
	 * Add a Sample to be played for given MIDI note.
	 * The entire sample will be played.
	 * 
	 * @param note the MIDI note
	 * @param sample the Sample
	 */
	public void addSample(int note, Sample sample) {
		sampleBank.put(note, sample);
	}
	
	/**
	 * Add a Sample to be played for given MIDI note.
	 * Only the specified range of the sample will be played.
	 * 
	 * @param note     the MIDI note
	 * @param sample   the Sample
	 * @param range    the SampleRange (start and end time)
	 */
	public void addSample(int note, Sample sample, SampleRange range) {
		sampleBank.put(note, sample);
		sampleRanges.put(note, range);
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
			PlayerInfo sp = new PlayerInfo();
			sp.player = player;
			sp.env = new Envelope(ac, 1.0f); // Controls gain
			sp.gain = new Gain(ac, 2);
			sp.gain.setGain(sp.env);
			sp.gain.addInput(sp.player);
			samplePlayers.put(entry.getKey(), sp);
			//System.out.printf("Added sample player for note %d\n", entry.getKey());
		}
		
		// All of the SamplePlayers' Gains feed into a mixer Gain,
		// which mixes the input
		UGen mixer = new Gain(ac, 2);
		for (PlayerInfo sp : samplePlayers.values()) {
			mixer.addInput(sp.gain);
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
					PlayerInfo sp = samplePlayers.get(note);
					if (sp != null) {
						double time = ac.getTime();
						System.out.printf("Play sample %d at %f ms\n", note, time);
						
						sp.player.reset();
						if (sampleRanges.containsKey(note)) {
							SampleRange range = sampleRanges.get(note);
							sp.player.setPosition(range.startMs);
						}
						
						sp.player.start();
					}
				}
			}
		}
	}
}
