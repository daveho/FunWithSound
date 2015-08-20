package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.Rhythm;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

public class Demo extends DemoBase {
	public void create() {
		melodicMinor(65);
		tempo(220, 8);
		
		Instrument oohs = instr(ARACHNO, 55);
		Instrument organ = instr(ARACHNO, 21);
		Instrument b = instr(ARACHNO, 78);

		Rhythm leadin = r(fs(0), qs(1), fs(2), fs(3), fs(4), qs(5), fs(6), qs(7));
		Melody low = m(-7, -6, n(-7, -5), -4, n(-5, -3), n(-6, -2), n(-6, -1), n(-5, 0));
		Melody mid = xm(1, low);
		Melody hi = xm(2, low);
		
		Rhythm pulse = r(s(0,4), s(4,4));
		Melody bassdrone = xm(-3, m(0, 0));
		Melody bassdrone2 = xm(-3, m(1, 1));
		Melody bassdrone3 = xm(-3, m(-4, -3));
		Melody bassdrone4 = xm(-3, m(n(-2, 4), 2));
		Melody bassdrone5 = xm(-3, m(0, 3));
		Melody bassdrone6 = xm(-3, m(0, -1));
		
		Melody ominous1 = xm(-2, m(n(0, 2), n(1, 3)));
		Melody ominous2 = xm(-2, m(0, n(2, 4)));
		Melody ominous3 = xm(-2, m(n(2, 5), n(1, 6)));
		
		Melody lead1 = xm(-1, m(n(0,2), n(-5,0)));
		Melody lead2 = xm(-1, m(n(0, -3, -7)));
		Melody lead3 = xm(0, m(n(-3, 3), n(0, 2), 4, 0, 3, 1, n(3, -3), n(2, -1)));
		Rhythm lead3r = r(fs(0), fs(1), fs(2), fs(3), fs(4), fs(5), fs(6), fs(7));
		
		Rhythm lead4r = r(s(0, 2), fs(3), fs(4), hs(5), s(6, 2));
		Melody lead4 = xm(-1, m(n(3, -3), n(3, -6), n(3, -5), n(-4, 4), n(3, -3)));
		Melody lead5 = xm(-1, m(n(2, 4, -7), n(-10, 1, 6)));
		Rhythm linger = r(s(0, 6.5));
		Melody lead6 = xm(-1, m(n(-7, 3, 7)));
		
		final Figure lf1 = f(leadin, low, oohs);
		final Figure lf2 = f(leadin, mid, oohs);
		final Figure lf3 = f(leadin, hi, oohs);
		
		final Figure bf = f(pulse, bassdrone, organ);
		Figure bf2 = f(pulse, bassdrone2, organ);
		Figure bf3 = f(pulse, bassdrone3, organ);
		final Figure bf4 = f(pulse, bassdrone4, organ);
		final Figure bf5 = f(pulse, bassdrone5, organ);
		final Figure bf6 = f(pulse, bassdrone6, organ);
		
		final Figure omf1 = f(pulse, ominous1, b);
		final Figure omf2 = f(pulse, ominous2, b);
		final Figure omf3 = f(pulse, ominous3, b);
		
		final Figure l1f = f(pulse, lead1, oohs);
		final Figure l2f = f(pulse, lead2, oohs);
		final Figure l3f = f(lead3r, lead3, oohs);
		
		Figure l4f = f(lead4r, lead4, oohs);
		Figure l5f = f(pulse, lead5, oohs);
		Figure l6f = f(linger, lead6, oohs);
		
		add(lf1);
		add(lf2);
		add(lf3);

		add(gf(lf1, bf));
		add(gf(lf2, bf));
		add(gf(lf3, bf));

		addseqn(2, gf(lf1, bf, omf1), gf(lf2, bf, omf2), gf(lf3, bf, omf3));
		
		addn(2, bf);

		addseqn(2, gf(bf4, l1f), gf(bf5, l2f), gf(bf6, l3f));

		addseqn(2, gf(bf4, xf(1, l1f)), gf(bf5, xf(1, l2f)), gf(bf6, xf(1, l3f)));
		
		add(gf(bf2, l4f));
		add(gf(bf3, l5f));
		add(gf(bf, l6f));
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo d = new Demo();
		d.demo();
	}
}
