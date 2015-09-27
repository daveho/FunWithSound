package io.github.daveho.funwithsound.demo;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Rhythm;

// This demo is just to test that the Gervill emergency soundbank
// can be used.
public class Demo9 extends DemoBase {
	@Override
	public void create() {
		tempo(220, 8);

		Instrument met = percussion();
		Instrument bb = instr(77);

		Rhythm metr = rr(p(0,101), 2, 32);
		Figure metf = pf(metr, 42, met);

		add1(metf);
		
		audition(bb);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo9 demo = new Demo9();
		demo.create();
		demo.play();
	}
}
