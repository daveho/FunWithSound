package io.github.daveho.funwithsound.demo;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import io.github.daveho.funwithsound.AddDelay;
import io.github.daveho.funwithsound.AddReverb;
import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.Rhythm;

public class Demo4 extends DemoBase {

	@Override
	public void create() {
		tempo(220, 8);
		major(51);
		
		Instrument drumkit = percussion(TR808);
		//addfx(drumkit, new AddReverb());
		addfx(drumkit, new AddDelay(250.0, .8, .4));
		
		
		Rhythm r = rr(p(0, 127), 2, 4);
		int note = 40;
		Melody m = m(an(note), an(note), an(note), an(note));
		Figure f = f(r, m, drumkit);
		
		
		add1n(4, f);
		
		
	}

	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo4 demo = new Demo4();
		demo.create();
		demo.play();
	}
}
