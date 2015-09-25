package io.github.daveho.funwithsound.demo;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.DataBead;
import io.github.daveho.funwithsound.CustomInstrumentFactory;
import io.github.daveho.funwithsound.CustomInstrumentFactoryImpl;
import io.github.daveho.funwithsound.Defaults;
import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.MonoSynthUGen2;
import io.github.daveho.funwithsound.ParamNames;
import io.github.daveho.funwithsound.Player;
import io.github.daveho.funwithsound.RealizedInstrument;
import io.github.daveho.funwithsound.Rhythm;
import io.github.daveho.funwithsound.SynthToolkit;
import io.github.daveho.funwithsound.SynthToolkitBuilder;

public class Waves extends DemoBase {

	@Override
	public void create() {
		tempo(220,8);
		
		Instrument synth = custom(4);
		v(synth, 0.2);
		
		Rhythm wr = r(s(0,8,127));
		Melody wm = m(an(69));
		Figure wf = f(wr,wm,synth);
		
		add1(wf);
	}
	
	private static class CreateWaveSynth implements CustomInstrumentFactoryImpl.CreateCustomInstrument {
		private Buffer waveform;
		private double[] freqMult;
		private double[] gains;
		
		public CreateWaveSynth(Buffer waveform) {
			this(waveform, new double[]{1.0}, new double[]{1.0});
		}
		
		public CreateWaveSynth(Buffer waveform, double[] freqMult, double[] gains) {
			this.waveform = waveform;
			this.freqMult = freqMult;
			this.gains = gains;
		}
		
		@Override
		public RealizedInstrument create(AudioContext ac) {
			DataBead params = Defaults.monosynthDefaults();
			params.put(ParamNames.GLIDE_TIME_MS, 0);
			SynthToolkit tk = SynthToolkitBuilder.start()
					.withWaveVoice(waveform)
					.withOnOffNoteEnvelope()
					.getTk();
			MonoSynthUGen2 u = new MonoSynthUGen2(ac, tk, params, freqMult, gains);
			return new RealizedInstrument(u, ac);
		}
	}
	
	@Override
	protected void onCreatePlayer(Player player) {
		CustomInstrumentFactory fac = new CustomInstrumentFactoryImpl(
				0, new CreateWaveSynth(Buffer.SINE),
				1, new CreateWaveSynth(Buffer.SQUARE),
				2, new CreateWaveSynth(Buffer.SAW),
				3, new CreateWaveSynth(Buffer.TRIANGLE),
				4, new CreateWaveSynth(Buffer.SINE, new double[]{1,2,3,4,5,6,7}, new double[]{1,.5,.2,.1,.05,.03,.01}));
		player.setCustomInstrumentFactory(fac);
		
		player.setStartDelayUs(50*1000L);
		player.setIdleWaitUs(50*1000L);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Waves demo = new Waves();
		demo.create();
		//demo.play();
		demo.saveWaveFile("sine440overtones.wav");
	}

}
