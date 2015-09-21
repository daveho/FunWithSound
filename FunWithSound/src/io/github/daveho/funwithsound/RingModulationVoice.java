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
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * A {@link Voice} implementation that uses ring modulation.
 * The modulation frequency is set to be a multiple of the
 * note frequency.  The modulation and carrier signals
 * can have their waveforms specified (sine, saw, square, etc.)
 */
public class RingModulationVoice implements ParamNames, Voice {
	private Glide modFreq;
	private float modFreqMultiple;
	private UGen output;
	
	/**
	 * Constructor.
	 * 
	 * @param ac the AudioContext
	 * @param params the parameters
	 * @param modWaveform the waveform for the modulation signal
	 * @param carrierWaveForm the waveform for the carrier signal
	 * @param freq the frequency controller
	 */
	public RingModulationVoice(AudioContext ac, DataBead params, Buffer modWaveform, Buffer carrierWaveForm, UGen freq) {
		// Glide the modulation frequency
		modFreq = new Glide(ac);
		modFreq.setGlideTime(Util.getFloat(params, MOD_GLIDE_TIME_MS, 0.0));
		
		// The modulation frequency is set to this multiple of
		// the note frequency
		modFreqMultiple = Util.getFloat(params, MOD_FREQ_MULTIPLE);
		
		// UGen to generate the modulation waveform
		UGen mod = new WavePlayer(ac, modFreq, modWaveform);
		UGen carrier = new WavePlayer(ac, freq, carrierWaveForm);
		UGen output = new Function(carrier, mod) {
			@Override
			public float calculate() {
				return x[0] * x[1];
			}
		};
		this.output = output;
	}

	@Override
	public UGen getOutput() {
		return output;
	}

	@Override
	public void noteOn(ShortMessage smsg, int note) {
		modFreq.setValue(modFreqMultiple * Pitch.mtof(note));
	}

	@Override
	public void noteOff(ShortMessage smsg, int note) {
		// Nothing to do
	}
}
