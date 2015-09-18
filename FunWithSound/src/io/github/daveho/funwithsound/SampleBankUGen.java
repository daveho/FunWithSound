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
	private static final float RAMP_TIME_MS = 5.0f;
	
	private static class PlayerInfo {
		SamplePlayer player;
		Envelope env;
		Gain out;
		SampleRange range;
	}
	
	private AudioContext ac;
	private Map<Integer, PlayerInfo> samplePlayers;
	private Gain mixer;

	/**
	 * Constructor.
	 * 
	 * @param ac the AudioContext
	 */
	public SampleBankUGen(AudioContext ac) {
		super(ac, 0, 2);
		this.ac = ac;
		samplePlayers = new HashMap<Integer, PlayerInfo>();

		// All of the SamplePlayers' Gains feed into a mixer Gain,
		// which mixes the input
		this.mixer = new Gain(ac, 2);
		addToChainOutput(mixer);
	}
	
	/**
	 * Add a Sample to be played for given MIDI note.
	 * The entire sample will be played.
	 * 
	 * @param note the MIDI note
	 * @param sample the Sample
	 * @param gain the gain
	 */
	public void addSample(int note, Sample sample, double gain) {
		addSample(note, sample, gain, new SampleRange(0, sample.getLength()));
	}
	
	/**
	 * Add a Sample to be played for given MIDI note.
	 * Only the specified range of the sample will be played.
	 * 
	 * @param note     the MIDI note
	 * @param sample   the Sample
	 * @param gain     the gain
	 * @param range    the SampleRange (start and end time)
	 */
	public void addSample(int note, Sample sample, double gain, SampleRange range) {
		PlayerInfo sp = new PlayerInfo();

		SamplePlayer player = new SamplePlayer(ac, 2);
		player.setSample(sample);
		player.setLoopType(LoopType.NO_LOOP_FORWARDS);
		player.setKillOnEnd(false);
		player.pause(true);
		
		sp.player = player;
		sp.env = new Envelope(ac, 0.0f); // Controls the sample gain envelope
		
		// Sample envelope gain
		Gain senvGain = new Gain(ac, 2);
		senvGain.setGain(sp.env);
		senvGain.addInput(sp.player);
		
		// Give subclasses an opportunity to capture the sample player
		// output and do something with it before it enters the
		// per-sample static gain
		UGen senvOut = createSampleOutput(ac, note, senvGain);
		
		// Create a single static Gain to control the volume of
		// the sample
		sp.out = new Gain(ac, 2);
		System.out.printf("Setting static gain for sample %d to %f\n", note, gain);
		sp.out.setGain((float)gain);
		sp.out.addInput(senvOut);
		
		// Range of the sample to be played
		sp.range = range;

		samplePlayers.put(note, sp);
		//System.out.printf("Added sample player for note %d\n", entry.getKey());
		
		// Feed the sample player's output into the mixer UGen
		mixer.addInput(sp.out);
	}

	/**
	 * Returns the UGen that should be the output of the
	 * sample player before it enters the per-sample gain UGen.
	 * By default, just returns the parameter (the sample player gain
	 * envelope UGen.) Subclasses may override to attach effects.
	 * 
	 * @param ac the AudioContext 
	 * @param note the MIDI note corresponding to this sample
	 * @param senvGain the output of the sampler player's gain envelope UGen
	 * @return sample player output
	 */
	protected UGen createSampleOutput(AudioContext ac, int note, Gain senvGain) {
		return senvGain;
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
					final PlayerInfo sp = samplePlayers.get(note);
					if (sp != null) {
//						double time = ac.getTime();
//						System.out.printf("Play sample %d at %f from %f..%f ms\n", note, time, sp.range.startMs, sp.range.endMs);
						sp.player.reset();
						sp.player.setPosition(sp.range.startMs);
						float durationMs = (float)(sp.range.endMs - sp.range.startMs);
						
						sp.env.clear();
						float middleTimeMs = durationMs - 2*RAMP_TIME_MS;
						sp.env.addSegment(1.0f, RAMP_TIME_MS);
						if (middleTimeMs > 0) {
							sp.env.addSegment(1.0f, middleTimeMs);
						}
						sp.env.addSegment(0.0f, RAMP_TIME_MS);
						
						sp.player.start();
					}
				}
			}
		}
	}
}
