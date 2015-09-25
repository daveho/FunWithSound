package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.ASRNoteEnvelope;
import io.github.daveho.funwithsound.AddReverb;
import io.github.daveho.funwithsound.CustomInstrumentFactoryImpl;
import io.github.daveho.funwithsound.Defaults;
import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.MonoSynthUGen2;
import io.github.daveho.funwithsound.NoteEnvelope;
import io.github.daveho.funwithsound.ParamNames;
import io.github.daveho.funwithsound.Player;
import io.github.daveho.funwithsound.RealizedInstrument;
import io.github.daveho.funwithsound.Rhythm;
import io.github.daveho.funwithsound.RingModulationVoice;
import io.github.daveho.funwithsound.SynthToolkit;
import io.github.daveho.funwithsound.Voice;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.DataBead;

public class Demo7 extends DemoBase {

	@Override
	public void create() {
		tempo(220, 8);
		Instrument tr808 = percussion(TR808);
		
		Instrument fmsynth = custom(0);
//		Instrument fmsynth = instr(TB303, 11);
//		addfx(fmsynth, new AddOscillatingBandPassFilter(200, 800, 4.0));
		addfx(fmsynth, new AddReverb());
		v(fmsynth, 0.9);
		
		Rhythm clickr = rr(p(0,88), 1.0, 256);
		Figure clickf = pf(clickr, 42, tr808);
		
		
		add1(clickf);
		
		audition(fmsynth);
	}
	
	@Override
	protected void onCreatePlayer(Player player) {
		CustomInstrumentFactoryImpl fac = new CustomInstrumentFactoryImpl(
				0, new CustomInstrumentFactoryImpl.CreateCustomInstrument() {
					@Override
					public RealizedInstrument create(AudioContext ac) {
						DataBead params = Defaults.monosynthDefaults();
						params.putAll(Defaults.bandpassNoteEnvelopeDefaults());
						params.putAll(Defaults.fmVoiceDefaults());
						params.put(ParamNames.GLIDE_TIME_MS, 20);
						params.put(ParamNames.START_END_FREQ_FACTOR, .5);
						params.put(ParamNames.RISE_FREQ_FACTOR, 1);
						
						params.put(ParamNames.MIN_FREQ_MULTIPLE, -1);
						params.put(ParamNames.MAX_FREQ_MULTIPLE, 1);
						
						SynthToolkit tk = new SynthToolkit() {
							@Override
							public Voice createVoice(AudioContext ac, DataBead params, UGen freq) {
								//return new WaveVoice(ac, Buffer.SQUARE, freq);
								return new RingModulationVoice(ac, params, Buffer.SAW, Buffer.SAW, freq);
								//return new FMVoice(ac, params, Buffer.SAW, Buffer.SAW, freq);
							}
							
							@Override
							public NoteEnvelope createNoteEnvelope(AudioContext ac, DataBead params, UGen input) {
								NoteEnvelope delegate = new ASRNoteEnvelope(ac, params, input);
								//BandpassFilterNoteEnvelopeAdapter adapter = new BandpassFilterNoteEnvelopeAdapter(ac, params, delegate);
								//return adapter;
								return delegate;
							}
						};
						
						MonoSynthUGen2 u = new MonoSynthUGen2(
								ac,
								tk,
								params,
								new double[]{1.0/*, 2.0, 4.0*/},
								new double[]{.5/*, .5, .1*/});
						return new RealizedInstrument(u, ac);
					}
				});
		player.setCustomInstrumentFactory(fac);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo7 demo = new Demo7();
		demo.create();
		demo.play();
	}
}
