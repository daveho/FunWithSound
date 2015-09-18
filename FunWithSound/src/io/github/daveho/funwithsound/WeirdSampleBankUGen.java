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

import java.util.HashMap;
import java.util.Map;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * Experiment to use the output of the SamplePlayers in a SampleBankUGen
 * to control the gain of a sine oscillator with a fixed frequency.
 * My theory is that it would sound weird and cool.
 * And it does.  
 */
public class WeirdSampleBankUGen extends SampleBankUGen {
	private Map<Integer, Double> noteFreqMap;
	
	/**
	 * Constructor.
	 * 
	 * @param ac the AudioContext
	 */
	public WeirdSampleBankUGen(AudioContext ac) {
		super(ac);
		noteFreqMap = new HashMap<Integer, Double>();
	}

	/**
	 * Add a "weird" sample.
	 * The sample will play in its entirety.
	 * 
	 * @param note the MIDI note
	 * @param sample the Sample
	 * @param gain the gain
	 * @param freq the frequency of the static sine oscillator
	 */
	public void addSample(int note, Sample sample, double gain, double freq) {
		noteFreqMap.put(note, freq);
		super.addSample(note, sample, gain);
	}
	
	/**
	 * Add a "weird" sample.
	 * Only the specified range of the sample will be played.
	 * 
	 * @param note     the MIDI note
	 * @param sample   the Sample
	 * @param gain     the gain
	 * @param range    the SampleRange (start and end time)
	 * @param freq the frequency of the static sine oscillator
	 */
	public void addSample(int note, Sample sample, double gain, SampleRange range, double freq) {
		noteFreqMap.put(note, freq);
		super.addSample(note, sample, gain, range);
	}
	
	@Override
	protected UGen createSampleOutput(AudioContext ac, int note, Gain senvGain) {
		if (!noteFreqMap.containsKey(note)) {
			// Just play the unmodified sample
			return senvGain;
		}
		float freq = noteFreqMap.get(note).floatValue();
//		System.out.printf("Note %d freq=%f\n", note, freq);
		WavePlayer sin = new WavePlayer(ac, freq, Buffer.SINE);
		Gain g = new Gain(ac, 2);
		g.setGain(senvGain);
		g.addInput(sin);
		return g;
	}
}
