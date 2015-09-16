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
import net.beadsproject.beads.data.DataBeadReceiver;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.BiquadFilter;
import net.beadsproject.beads.ugens.Envelope;

/**
 * Variant of {@link MonoSynthUGen} that processes played
 * notes with a bandpass filter.  The bandpass filter's center frequency
 * glides from a start frequency to a "rise" frequency, and then glides
 * from the rise frequency back to the start frequency.
 * Accepts parameter configuration via a DataBead.
 */
public class BandpassFilterMonoSynthUGen extends MonoSynthUGen implements DataBeadReceiver {
	/** DataBead property name: Start frequency (expressed as a multiple of the note frequency). */
	public static final String START_END_FREQ_FACTOR = "startEndFreqFactor";
	/** DataBead property name: Rise frequency (expressed as a multiple of the note frequency). */
	public static final String RISE_FREQ_FACTOR = "riseFreqFactor";
	/** DataBead property name: Time to rise from the start frequency to the rise frequency. */
	public static final String RISE_TIME_MS = "riseTimeMs";
	/** DataBead property name: Time to decay from the rise frequency back to the start frequency. */
	public static final String DECAY_TIME_MS = "decayTimeMs";
	/** DataBead property name: Curvature of the glides from start to rise and back. */
	public static final String CURVATURE = "curvature";
	
	/**
	 * Get the default parameters.
	 * 
	 * @return the default parameters
	 */
	public static DataBead defaultParams() {
		DataBead params = new DataBead();
		MonoSynthUGen.setToDefault(params);
		params.put(START_END_FREQ_FACTOR, .25f);
		params.put(RISE_FREQ_FACTOR, 4f);
		params.put(RISE_TIME_MS, 200f);
		params.put(DECAY_TIME_MS, 1000f);
		params.put(CURVATURE, .25);
		return params;
	}
	
	private Envelope centerFreqEnv;
	private BiquadFilter filter;

	/**
	 * Constructor.
	 * 
	 * @param ac      the AudioContext
	 * @param buffer  the buffer type (sine, square, triangle, etc.)
	 * @param params  parameters to control attack/decay, glide time, etc.
	 */
	public BandpassFilterMonoSynthUGen(AudioContext ac, Buffer buffer, DataBead params) {
		super(ac, buffer, params);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param ac      the AudioContext
	 * @param buffer  the buffer type (sine, square, triangle, etc.)
	 * @param params  parameters to control attack/decay, glide time, etc.
	 * @param freqMult create oscillators to play these multiples of the note frequency 
	 */
	public BandpassFilterMonoSynthUGen(AudioContext ac, Buffer buffer, DataBead params, double[] freqMult) {
		super(ac, buffer, params, freqMult);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param ac      the AudioContext
	 * @param buffer  the buffer type (sine, square, triangle, etc.)
	 * @param params  parameters to control attack/decay, glide time, etc.
	 * @param freqMult create oscillators to play these multiples of the note frequency 
	 * @param oscGains the gains for each oscillator
	 */
	public BandpassFilterMonoSynthUGen(AudioContext ac, Buffer buffer, DataBead params, double[] freqMult, double[] oscGains) {
		super(ac, buffer, params, freqMult, oscGains);
	}
	
	@Override
	protected UGen createOutputUGen(AudioContext ac, UGen tail) {
		centerFreqEnv = new Envelope(ac);
		filter = new BiquadFilter(ac, 2, BiquadFilter.Type.BP_SKIRT);
		filter.setFrequency(centerFreqEnv);
		
		filter.addInput(tail);
		return filter;
	}
	
	@Override
	protected void onNoteOn(ShortMessage smsg, int note) {
		DataBead params = getParams();
		
		super.onNoteOn(smsg, note);
		centerFreqEnv.clear();
		float minFreq = Util.getFloat(params, START_END_FREQ_FACTOR) * Pitch.mtof(note);
		float maxFreq = Util.getFloat(params, RISE_FREQ_FACTOR) * Pitch.mtof(note);
		centerFreqEnv.setValue(minFreq);
		
		float curvature = Util.getFloat(params, CURVATURE);
		centerFreqEnv.addSegment(maxFreq, Util.getFloat(params, RISE_TIME_MS), curvature);
		centerFreqEnv.addSegment(minFreq, Util.getFloat(params, DECAY_TIME_MS), curvature);
	}

	@Override
	public DataBeadReceiver sendData(DataBead data) {
		getParams().clear();
		getParams().putAll(data);
		return this;
	}
}
