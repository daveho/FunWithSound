package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.AddDelay;
import io.github.daveho.funwithsound.AddFlanger;
import io.github.daveho.funwithsound.AddReverb;
import io.github.daveho.funwithsound.CustomInstrumentFactory;
import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.InstrumentInfo;
import io.github.daveho.funwithsound.MonoSynthUGen;
import io.github.daveho.funwithsound.Player;
import io.github.daveho.funwithsound.Rhythm;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;

public class Demo6 extends DemoBase {
	@Override
	public void create() {
		tempo(220, 8);
		
		Instrument tr808 = percussion(TR808);
		
		Rhythm clickr = rr(s(0,.5,101), 1, 128);
		Figure clickf = pf(clickr, 42, tr808);
		
		Instrument monosynth = custom(0);
		v(monosynth, 0.1);
		//addfx(monosynth, new AddDelay(200, 1.0, 0.6));
		AddFlanger.Params params = AddFlanger.defaultParams();
		addfx(monosynth, new AddFlanger(params));
		addfx(monosynth, new AddReverb());
		
		add1(clickf);
		
		audition(monosynth);
	}
	
	@Override
	protected void onCreatePlayer(Player player) {
		CustomInstrumentFactory fac = new CustomInstrumentFactory() {
			@Override
			public InstrumentInfo create(int code, AudioContext ac) {
				if (code == 0) {
					MonoSynthUGen synth = new MonoSynthUGen(ac, Buffer.SAW);
					return new InstrumentInfo(synth, ac);
				}
				throw new IllegalArgumentException("Don't know how to create custom instrument " + code);
			}
		};
		player.setCustomInstrumentFactory(fac);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo6 demo = new Demo6();
		demo.create();
		demo.play();
	}
}
