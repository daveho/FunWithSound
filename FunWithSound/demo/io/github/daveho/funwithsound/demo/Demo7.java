package io.github.daveho.funwithsound.demo;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.DataBead;
import io.github.daveho.funwithsound.AddReverb;
import io.github.daveho.funwithsound.CustomInstrumentFactoryImpl;
import io.github.daveho.funwithsound.FMMonoSynthUGen;
import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.MonoSynthUGen;
import io.github.daveho.funwithsound.Player;
import io.github.daveho.funwithsound.RealizedInstrument;
import io.github.daveho.funwithsound.Rhythm;

public class Demo7 extends DemoBase {

	@Override
	public void create() {
		tempo(220, 8);
		Instrument tr808 = percussion(TR808);
		
		Instrument fmsynth = custom(0);
//		Instrument fmsynth = instr(TB303, 11);
		addfx(fmsynth, new AddReverb());
		v(fmsynth, 0.4);
		
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
						FMMonoSynthUGen u = new FMMonoSynthUGen(
								ac,
								Buffer.SINE,
								params,
								new double[]{1.0/*, 2.0, 4.0*/},
								new double[]{1.0/*, .5, .1*/});
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
