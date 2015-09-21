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

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.core.UGenChain;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.data.DataBeadReceiver;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;

/**
 * Modular monosynth UGen.  Can be customized using
 * {@link Voice} and {@link NoteEnvelope} objects.
 * Each rendered note consists of arbitrary multiples of the
 * base note frequency, each with an arbitrary static
 * gain.
 * Accepts parameter configuration via a DataBead.
 */
public class MonoSynthUGen2 extends UGenChain implements ParamNames, DataBeadReceiver {
	private DataBead params;
	private Glide freq;
	private Voice[] voices;
	private NoteEnvelope noteEnv;
	private int note;
	
	/**
	 * Constructor.
	 * The synth will play multiple frequencies when a note is played.
	 * Each frequency has a specified static gain.
	 * 
	 * @param ac      the AudioContext
	 * @param toolkit the {@link SynthToolkit} to use to create voices,
	 *                note envelope, etc.
	 * @param params  parameters to control attack/decay, glide time, etc.
	 * @param freqMult create voices to play these multiples of the note frequency 
	 * @param oscGains the gains for each oscillator
	 */
	public MonoSynthUGen2(AudioContext ac,
			SynthToolkit toolkit,
			DataBead params,
			double[] freqMult,
			double[] oscGains) {
		super(ac, 0, 2);
		
		this.params = params;

		// Glide to control the note base frequency
		freq = new Glide(ac);
		freq.setGlideTime(Util.getFloat(params, GLIDE_TIME_MS)); // For portamento

		// Mixer for all of the Voice outputs
		Gain mixer = new Gain(ac, 2);

		// Create Voices to play frequencies that are multiples of
		// the note frequency
		this.voices = new Voice[freqMult.length];
		Gain[] outGains = new Gain[freqMult.length];
		for (int i = 0; i < freqMult.length; i++) {
			// UGen to multiply the note base frequency by the appropriate multiple
			UGen multFreq = Util.multiply(freq, freqMult[i]);
			
			// Create a Voice for this multiple
			voices[i] = toolkit.createVoice(ac, params, multFreq);
			
			// Create a static Gain for this Voice
			outGains[i] = new Gain(ac, 2);
			outGains[i].setGain((float)oscGains[i]);
			outGains[i].addInput(voices[i].getOutput());
			
			// Mix Voice output
			mixer.addInput(outGains[i]);
		}
		
		// Create a note envelope
		noteEnv = toolkit.createNoteEnvelope(ac, params, mixer);

		// Use the note envelope to control the gain of the voice mixer
		UGen output = noteEnv.getOutput();
		
		// Adapt the output (add effects, etc.)
		output = createOutputUGen(ac, output);

		addToChainOutput(output);
	}
	
	/**
	 * Get parameters.
	 * 
	 * @return the parameters
	 */
	public DataBead getParams() {
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
						onNoteOff(smsg, note);
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
		// Keep track of current note
		this.note = note;

		// Glide to note frequency
		freq.setValue(Pitch.mtof(note));
		
		// Notify Voices of note starting
		for (Voice v : voices) {
			v.noteOn(smsg, note);
		}
		
		// Notify note envelope of note starting
		noteEnv.noteOn(smsg, note);
	}

	/**
	 * Called when a NOTE_OFF message turning off the current note is received,
	 * and playing the current note should end.
	 * Subclasses may override (but should call this method
	 * as part of their implementation.)
	 * 
	 * @param note the MIDI note number
	 */
	protected void onNoteOff(ShortMessage smsg, int note) {
		// Notify voices of note off
		for (Voice v : voices) {
			v.noteOff(smsg, note);
		}
		
		// Notify note envelope of note off
		noteEnv.noteOff(smsg, note);
	}

	@Override
	public DataBeadReceiver sendData(DataBead data) {
		params.clear();
		params.putAll(data);
		return this;
	}
}
