package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.ASRNoteEnvelope;
import io.github.daveho.funwithsound.AddOscillatingBandPassFilter;
import io.github.daveho.funwithsound.AddReverb;
import io.github.daveho.funwithsound.CustomInstrumentFactoryImpl;
import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.MonoSynthUGen;
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
		addfx(fmsynth, new AddOscillatingBandPassFilter(200, 800, 4.0));
		addfx(fmsynth, new AddReverb());
		v(fmsynth, 0.7);
		
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
						DataBead params = MonoSynthUGen.defaultParams();
						params.put(MonoSynthUGen.GLIDE_TIME_MS, 80);
						SynthToolkit tk = new SynthToolkit() {
							@Override
							public Voice createVoice(AudioContext ac, DataBead params, UGen freq) {
								//return new WaveVoice(ac, Buffer.SQUARE, freq);
								DataBead vParams = new DataBead();
								vParams.putAll(params);
								vParams.put(ParamNames.MOD_FREQ_MULTIPLE, 2);
								vParams.put(ParamNames.MOD_GLIDE_TIME_MS, 10);
								return new RingModulationVoice(ac, vParams, Buffer.SAW, Buffer.SAW, freq);
							}
							
							@Override
							public NoteEnvelope createNoteEnvelope(AudioContext ac, DataBead params, UGen input) {
								return new ASRNoteEnvelope(ac, params, input);
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
