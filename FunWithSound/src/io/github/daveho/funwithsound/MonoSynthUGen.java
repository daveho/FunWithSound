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
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.core.UGenChain;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * This is basically an experiment to try to create a custom
 * MIDI mono synth using Beads, and use it to render
 * parts of a composition (as the runtime implementation of
 * a custom {@link Instrument}.)  It actually sounds pretty
 * decent!
 */
public class MonoSynthUGen extends UGenChain {
	/**
	 * Parameters.
	 */
	public static class Params {
		/** Glide time between notes (for portamento). */
		public double glideTimeMs;
		/** Time to ramp up to full gain when note starts. */
		public double attackTimeMs;
		/** Time to decay to silence when note ends. */
		public double decayTimeMs;
		/** Minimum gain (for notes with velocity 0.) */
		public double minGain;
	}
	
	/**
	 * Get default parameters.  These are abitrary, but sound pretty good.
	 * @return
	 */
	public static Params defaultParams() {
		Params params = new Params();
		setToDefault(params);
		return params;
	}

	public static void setToDefault(Params params) {
		params.glideTimeMs = 200;
		params.attackTimeMs = 20;
		params.decayTimeMs = 200;
		params.minGain = .1f;
	}
	
	private Params params;
	private double[] freqMult; // what frequencies are played (multiples of the note frequency)
	private Glide freq;
	private WavePlayer[] player;
	private Envelope gainEnv;
	private Gain gain;
	private Gain[] outGains;
	private int note;

	/**
	 * Constructor.
	 * The synth will play a single frequency when a note is played.
	 * 
	 * @param ac      the AudioContext
	 * @param buffer  the buffer type (sine, square, triangle, etc.)
	 * @param params  parameters to control attack/decay, glide time, etc.
	 */
	public MonoSynthUGen(AudioContext ac, Buffer buffer, Params params) {
		this(ac, buffer, params, new double[]{ 1.0 });
	}
	
	/**
	 * Constructor.
	 * The synth will play multiple frequencies when a note is played.
	 * 
	 * @param ac      the AudioContext
	 * @param buffer  the buffer type (sine, square, triangle, etc.)
	 * @param params  parameters to control attack/decay, glide time, etc.
	 * @param freqMult create oscillators to play these multiples of the note frequency 
	 */
	public MonoSynthUGen(AudioContext ac, Buffer buffer, Params params, double[] freqMult) {
		this(ac, buffer, params, freqMult, Util.filledDoubleArray(freqMult.length, 1.0));
	}
	
	/**
	 * Constructor.
	 * The synth will play multiple frequencies when a note is played.
	 * Each frequency has a specified static gain.
	 * 
	 * @param ac      the AudioContext
	 * @param buffer  the buffer type (sine, square, triangle, etc.)
	 * @param params  parameters to control attack/decay, glide time, etc.
	 * @param freqMult create oscillators to play these multiples of the note frequency 
	 * @param oscGains the gains for each oscillator
	 */
	public MonoSynthUGen(AudioContext ac, Buffer buffer, Params params, double[] freqMult, double[] oscGains) {
		super(ac, 0, 2);
		
		this.params = params;
		this.freqMult = freqMult;
		
		freq = new Glide(ac);
		gainEnv = new Envelope(ac);
		gain = new Gain(ac, 2, gainEnv);

		// Create WavePlayers to play frequencies that are multiples of
		// the note frequency
		this.player = new WavePlayer[freqMult.length];
		this.outGains = new Gain[freqMult.length];
		for (int i = 0; i < freqMult.length; i++) {
			final int index = i;
			Function multFreq = new Function(freq) {
				@Override
				public float calculate() {
					return (float)MonoSynthUGen.this.freqMult[index] * x[0];
				}
			};
			player[i] = new WavePlayer(ac, multFreq, buffer);
			
			outGains[i] = new Gain(ac, 2);
			outGains[i].setGain((float)oscGains[i]);

			outGains[i].addInput(player[i]);
			
			gain.addInput(outGains[i]);
		}
		
		freq.setGlideTime((float)params.glideTimeMs);
		
		UGen output = gain;
		output = createOutputUGen(ac, output);

		addToChainOutput(output);
	}
	
	/**
	 * Get parameters.
	 * 
	 * @return the parameters
	 */
	public Params getParams() {
		return params;
	}
	
	/**
	 * Downcall method to create an output UGen for playing one
	 * of the synth's frequencies.  Subclasses may override to add
	 * effects.
	 * 
	 * @param ac the AudioContext
	 * @param tail the output of the oscillator and gain envelope
	 * @return the output UGen: by default, returns the parameter
	 */
	protected UGen createOutputUGen(AudioContext ac, UGen tail) {
		return tail;
	}

	/**
	 * Get the MIDI note that the synthesizer is currently playing.
	 * 
	 * @return the current note
	 */
	public int getCurrentNote() {
		return note;
	}
	
	@Override
	protected void messageReceived(Bead message) {
		if (Midi.hasMidiMessage(message)) {
			MidiMessage msg = Midi.getMidiMessage(message);
			
			if (msg instanceof ShortMessage) {
				ShortMessage smsg = (ShortMessage) msg;
				int note = smsg.getData1();
				
				if (smsg.getCommand() == ShortMessage.NOTE_ON) {
					onNoteOn(smsg, note);
				} else if (smsg.getCommand() == ShortMessage.NOTE_OFF) {
					// Only requests to stop playing the current note will be honored
					if (note == this.note) {
						onNoteOff(note);
					}
				}
			}
		}
	}

	/**
	 * Called when a NOTE_ON message is received, and playing the
	 * specified note should start.
	 * Subclasses may override (but should call this method
	 * as part of their implementation.)
	 * 
	 * @param smsg the ShortMessage specifying the NOTE_ON message
	 * @param note the MIDI note number
	 */
	protected void onNoteOn(ShortMessage smsg, int note) {
		gainEnv.clear();
		
		int velocity = smsg.getData2();
		float gain = (float)params.minGain + ((1.0f - (float)params.minGain) * velocity/127.0f);
		gainEnv.addSegment(gain, (float)params.attackTimeMs);
		freq.setValue(Pitch.mtof(note));
		this.note = note;
	}

	/**
	 * Called when a NOTE_OFF message turning off the current note is received,
	 * and playing the current note should end.
	 * Subclasses may override (but should call this method
	 * as part of their implementation.)
	 * 
	 * @param note the MIDI note number
	 */
	protected void onNoteOff(int note) {
		gainEnv.addSegment(0.0f, (float)params.decayTimeMs);
	}
}
