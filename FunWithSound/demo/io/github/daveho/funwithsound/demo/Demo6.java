package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.AbstractCustomInstrumentFactory;
import io.github.daveho.funwithsound.AddDelay;
import io.github.daveho.funwithsound.AddFlanger;
import io.github.daveho.funwithsound.AddReverb;
import io.github.daveho.funwithsound.BandpassFilterMonoSynthUGen;
import io.github.daveho.funwithsound.CustomInstrumentFactory;
import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.InstrumentInfo;
import io.github.daveho.funwithsound.Player;
import io.github.daveho.funwithsound.Rhythm;
import io.github.daveho.funwithsound.Util;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;

public class Demo6 extends DemoBase {
	@Override
	public void create() {
		tempo(150, 8);
		
		Instrument tr808 = percussion(TR909);
		
		Rhythm clickr = rr(s(0,.5,101), 1, 4096);
		Figure clickf = pf(clickr, 42, tr808);
		
		Instrument monosynth = custom(0);
		v(monosynth, 0.6);
		//addfx(monosynth, new AddDelay(200, 1.0, 0.6));
		AddFlanger.Params params = AddFlanger.defaultParams();
		params.minDelayMs = 0;
		params.maxDelayMs = 5;
		params.freqHz = 1.0;
		addfx(monosynth, new AddFlanger(params));
		for (int i = 0; i < 8; i++) {
			addfx(monosynth, new AddDelay(i*100, 1.0, 0.3));
		}
//		addfx(monosynth, new AddDelay(100, 1.0, 0.6));
//		addfx(monosynth, new AddDelay(200, 1.0, 0.5));
//		addfx(monosynth, new AddDelay(300, 1.0, 0.4));
//		addfx(monosynth, new AddDelay(400, 1.0, 0.3));
//		addfx(monosynth, new AddOscillatingBandPassFilter(400, 2000, .1));
//		addfx(monosynth, new AddReverb());
		
		add1(clickf);
		
		audition(monosynth);
	}
	
	@Override
	protected void onCreatePlayer(Player player) {
		CustomInstrumentFactory fac = new AbstractCustomInstrumentFactory() {{
			addCreator(0, new CreateCustomInstrument() {
				@Override
				public InstrumentInfo create(AudioContext ac) {
					BandpassFilterMonoSynthUGen.Params params = BandpassFilterMonoSynthUGen.defaultParams();
					params.attackTimeMs = 10;
					params.glideTimeMs = 40;
					params.startEndFreqFactor = .5;
					params.riseFreqFactor = 4;
					params.curvature = .25;
					BandpassFilterMonoSynthUGen synth = new BandpassFilterMonoSynthUGen(
							ac,
							Buffer.SINE,
							params,
							new double[]{1.0, /*Util.freqShift(7), 2.0, 2.0*Util.freqShift(7), 4.0,*/ 3.0},
							new double[]{1.0, /*0.2, 0.5, 0.1, 0.4,*/ 0.7});
					return new InstrumentInfo(synth, ac);
				}
			});
		}};
		player.setCustomInstrumentFactory(fac);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo6 demo = new Demo6();
		demo.create();
		demo.play();
	}
}
