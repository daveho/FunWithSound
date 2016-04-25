package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.Rhythm;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

// This demo is just to test that the Gervill emergency soundbank
// can be used.
public class Demo9 extends DemoBase {
	@Override
	public void create() {
		tempo(220, 8);
		naturalMinor(57); // A minor, rooted at A3

		Instrument met = percussion();
		Instrument bb = instr(89);

		Rhythm metr = rr(p(0,101), 2, 16);
		Figure metf = pf(metr, 42, met);

		Rhythm rhythm = r(
				s(0.000,3.925,110),s(3.858,4.133,102),s(7.932,4.121,90),s(12.054,4.058,93),s(16.012,1.014,87),s(16.938,3.159,93),s(20.140,4.006,106),s(24.163,3.956,69),s(28.098,1.643,58)
				);
		Melody melody = m(
				-3,3,1,-1,0,1,-3,5,3
				);

		Figure ff = f(rhythm, melody, bb);


//		add1(gf(metf));
		add1(gf(metf, ff));

		audition(bb);
	}

	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo9 demo = new Demo9();
		demo.create();
		demo.play();
	}
}
