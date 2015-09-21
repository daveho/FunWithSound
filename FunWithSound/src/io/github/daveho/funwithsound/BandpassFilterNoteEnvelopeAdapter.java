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
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.BiquadFilter;
import net.beadsproject.beads.ugens.Envelope;

/**
 * {@link NoteEnvelope} implementation that adapts another note envelope by
 * processing its output using a bandpass filter.
 */
public class BandpassFilterNoteEnvelopeAdapter implements ParamNames, NoteEnvelope {
	private DataBead params;
	private Envelope centerFreqEnv;
	private BiquadFilter filter;
	private NoteEnvelope delegate;
	
	public BandpassFilterNoteEnvelopeAdapter(AudioContext ac, DataBead params, NoteEnvelope delegate) {
		this.params = params;
		this.centerFreqEnv = new Envelope(ac);
		this.filter = new BiquadFilter(ac, 2, BiquadFilter.Type.BP_SKIRT);
		this.filter.setFrequency(this.centerFreqEnv);
		this.delegate = delegate;
		this.filter.addInput(delegate.getOutput());
	}

	@Override
	public UGen getOutput() {
		return filter;
	}

	@Override
	public void noteOn(ShortMessage smsg, int note) {
		delegate.noteOn(smsg, note);
		centerFreqEnv.clear();
		float minFreq = Util.getFloat(params, START_END_FREQ_FACTOR) * Pitch.mtof(note);
		float maxFreq = Util.getFloat(params, RISE_FREQ_FACTOR) * Pitch.mtof(note);
		centerFreqEnv.setValue(minFreq);
		
		float curvature = Util.getFloat(params, CURVATURE);
		centerFreqEnv.addSegment(maxFreq, Util.getFloat(params, RISE_TIME_MS), curvature);
		centerFreqEnv.addSegment(minFreq, Util.getFloat(params, FALL_TIME_MS), curvature);
	}

	@Override
	public void noteOff(ShortMessage smsg, int note) {
		delegate.noteOff(smsg, note);
	}
}
