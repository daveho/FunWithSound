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

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.DataBead;

/**
 * Fluid builder for creating a {@link SynthToolkit} using
 * the available {@link Voice} and {@link NoteEnvelope} implementations.
 */
public class SynthToolkitBuilder {
	private static class Endpoint implements SynthToolkit {
		@Override
		public Voice createVoice(AudioContext ac, DataBead params, UGen freq) {
			throw new UnsupportedOperationException("This toolkit does not specify a voice implementation");
		}

		@Override
		public NoteEnvelope createNoteEnvelope(AudioContext ac, DataBead params, UGen input) {
			throw new UnsupportedOperationException("This toolkit does not specify a note envelope implementation");
		}
	}
	
	private static class DelegatingSynthToolkit implements SynthToolkit {
		protected SynthToolkit delegate;
		
		public DelegatingSynthToolkit(SynthToolkit delegate) {
			this.delegate = delegate;
		}
		
		@Override
		public Voice createVoice(AudioContext ac, DataBead params, UGen freq) {
			return delegate.createVoice(ac, params, freq);
		}

		@Override
		public NoteEnvelope createNoteEnvelope(AudioContext ac, DataBead params, UGen input) {
			return delegate.createNoteEnvelope(ac, params, input);
		}
	}

	private SynthToolkit tk;
	
	/**
	 * Constructor.
	 */
	private SynthToolkitBuilder() {
		this.tk = new Endpoint();
	}
	
	/**
	 * Start a new {@link SynthToolkitBuilder}.
	 * 
	 * @return the new {@link SynthToolkitBuilder}
	 */
	public static SynthToolkitBuilder start() {
		return new SynthToolkitBuilder();
	}
	
	/**
	 * Get the completed {@link SynthToolkit}.
	 * 
	 * @return the completed {@link SynthToolkit}
	 */
	public SynthToolkit getTk() {
		return tk;
	}

	/**
	 * Specify that {@link WaveVoice} should be used as the {@link Voice} implementation.
	 * 
	 * @param waveform the voice waveform
	 * @return this object (for method chaining)
	 */
	public SynthToolkitBuilder withWaveVoice(final Buffer waveform) {
		return pushAdapter(new DelegatingSynthToolkit(this.tk) {
			@Override
			public Voice createVoice(AudioContext ac, DataBead params, UGen freq) {
				return new WaveVoice(ac, waveform, freq);
			}
		});
	}

	/**
	 * Specify that {@link RingModulationVoice} should be used as the {@link Voice} implementation.
	 * 
	 * @param modWaveform the modulator waveform
	 * @param carrierWaveform the carrier waveform
	 * @return this object (for method chaining)
	 */
	public SynthToolkitBuilder withRingModulationVoice(final Buffer modWaveform, final Buffer carrierWaveform) {
		return pushAdapter(new DelegatingSynthToolkit(this.tk) {
			@Override
			public Voice createVoice(AudioContext ac, DataBead params, UGen freq) {
				return new RingModulationVoice(ac, params, modWaveform, carrierWaveform, freq);
			}
		});
	}
	
	/**
	 * Specify that {@link OnOffNoteEnvelope} should be used as the {@link NoteEnvelope}.
	 * 
	 * @return this object (for method chaining)
	 */
	public SynthToolkitBuilder withOnOffNoteEnvelope() {
		return pushAdapter(new DelegatingSynthToolkit(this.tk) {
			public NoteEnvelope createNoteEnvelope(AudioContext ac, DataBead params, UGen input) {
				return new OnOffNoteEnvelope(ac, input);
			}
		});
	}
	
	/**
	 * Specify that {@link ASRNoteEnvelope} should be used as the {@link NoteEnvelope} implementation.
	 * 
	 * @return this object (for method chaining)
	 */
	public SynthToolkitBuilder withASRNoteEnvelope() {
		return pushAdapter(new DelegatingSynthToolkit(this.tk) {
			@Override
			public NoteEnvelope createNoteEnvelope(AudioContext ac, DataBead params, UGen input) {
				return new ASRNoteEnvelope(ac, params, input);
			}
		});
	}
	
	/**
	 * Specify that a {@link BandpassFilterNoteEnvelopeAdapter} should be used
	 * to adapt the previously specified {@link NoteEnvelope}.
	 * 
	 * @return this object (for method chaining)
	 */
	public SynthToolkitBuilder withBandpassFilterNoteEnvelopeAdapter() {
		return pushAdapter(new DelegatingSynthToolkit(this.tk) {
			@Override
			public NoteEnvelope createNoteEnvelope(AudioContext ac, DataBead params, UGen input) {
				NoteEnvelope delegateEnv = delegate.createNoteEnvelope(ac, params, input);
				return new BandpassFilterNoteEnvelopeAdapter(ac, params, delegateEnv);
			}
		});
	}
	
	private SynthToolkitBuilder pushAdapter(SynthToolkit adapter) {
		this.tk = adapter;
		return this;
	};
}
