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
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.BiquadFilter;
import net.beadsproject.beads.ugens.Envelope;

/**
 * Variant of {@link MonoSynthUGen} that processes played
 * notes with a bandpass filter.  The bandpass filter's center frequency
 * glides from a start frequency to a "rise" frequency, and then glides
 * from the rise frequency back to the start frequency.
 */
public class BandpassFilterMonoSynthUGen extends MonoSynthUGen {
	public static class Params extends MonoSynthUGen.Params {
		/** Start frequency (expressed as a multiple of the note frequency). */
		public double startEndFreqFactor;
		/** Rise frequency (expressed as a multiple of the note frequency). */
		public double riseFreqFactor;
		/** Time to rise from the start frequency to the rise frequency. */
		public double riseTimeMs;
		/** Time to decay from the rise frequency back to the start frequency. */
		public double decayTimeMs;
		/** Curvature of the glides from start to rise and back. */
		public double curvature;
	}
	
	/**
	 * Get the default parameters.
	 * 
	 * @return the default parameters
	 */
	public static Params defaultParams() {
		Params params = new Params();
		MonoSynthUGen.setToDefault(params);
		params.startEndFreqFactor = .25;
		params.riseFreqFactor = 4;
		params.riseTimeMs = 200;
		params.decayTimeMs = 1000;
		params.curvature = .25;
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
	 * @param freqMult create oscillators to play these multiples of the note frequency 
	 * @param oscGains the gains for each oscillator
	 */
	public BandpassFilterMonoSynthUGen(AudioContext ac, Buffer buffer, Params params, double[] freqMult, double[] oscGains) {
		super(ac, buffer, params, freqMult, oscGains);
	}
	
	@Override
	public Params getParams() {
		return (Params) super.getParams();
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
		super.onNoteOn(smsg, note);
		centerFreqEnv.clear();
		float minFreq = (float)(getParams().startEndFreqFactor * Pitch.mtof(note));
		float maxFreq = (float)(getParams().riseFreqFactor * Pitch.mtof(note));
		centerFreqEnv.setValue(minFreq);
		
		centerFreqEnv.addSegment(maxFreq, (float)getParams().riseTimeMs, (float)getParams().curvature);
		centerFreqEnv.addSegment(minFreq, (float)getParams().decayTimeMs, (float)getParams().curvature);
	}

}
