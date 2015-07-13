package edu.ycp.cs.funwithsound.demo;

import javax.sound.midi.MidiUnavailableException;

import edu.ycp.cs.funwithsound.Composer;
import edu.ycp.cs.funwithsound.Figure;
import edu.ycp.cs.funwithsound.Instrument;
import edu.ycp.cs.funwithsound.Melody;
import edu.ycp.cs.funwithsound.Player;
import edu.ycp.cs.funwithsound.Rhythm;
import edu.ycp.cs.funwithsound.Scale;
import edu.ycp.cs.funwithsound.Tempo;

public class Demo extends Composer {
	public void create() {
		setScale(Scale.melodicMinor(65));
		setTempo(new Tempo(240, 8));
		
		Instrument oohs = new Instrument(54);
		Instrument organ = new Instrument(20);
		Instrument b = new Instrument(77);
		Instrument f = new Instrument(105);

		Rhythm leadin = r(fs(0), qs(1), fs(2), fs(3), fs(4), qs(5), fs(6), qs(7));
		Melody low = m(-7, -6, n(-7, -5), -4, n(-5, -3), n(-6, -2), n(-6, -1), n(-5, 0));
		Melody mid = xo(1, low);
		Melody hi = xo(2, low);
		
		Rhythm pulse = r(s(0,4), s(4,4));
		Melody bassdrone = xo(-3, m(0, 0));
		Melody bassdrone2 = xo(-3, m(1, 1));
		Melody bassdrone3 = xo(-3, m(-4, -3));
		
		Melody ominous1 = xo(-2, m(n(0, 2), n(1, 3)));
		Melody ominous2 = xo(-2, m(0, n(2, 4)));
		Melody ominous3 = xo(-2, m(n(2, 5), n(1, 6)));
		
		Melody lead1 = m(2, 0);
		Melody lead2 = m(n(0, -3, -7));
		Melody lead3 = m(n(-3, 3), n(0, 2), 4, 0, 3, 1, n(3, -3), n(2, -1));
		Rhythm lead3r = r(fs(0), fs(1), fs(2), fs(3), fs(4), fs(5), fs(6), fs(7));
		
		Rhythm lead4r = r(s(0, 2), fs(3), fs(4), hs(5), s(6, 2));
		Melody lead4 = xo(-1, m(n(3, -3), n(3, -6), n(3, -5), n(-4, 4), n(3, -3)));
		Melody lead5 = xo(-1, m(n(2, 4, -7), n(-10, 1, 6)));
		Rhythm linger = r(s(0, 6.5));
		Melody lead6 = xo(-1, m(n(-7, 3, 7)));
		
		Figure lf1 = f(leadin, low, oohs);
		Figure lf2 = f(leadin, mid, oohs);
		Figure lf3 = f(leadin, hi, oohs);
		
		Figure bf = f(pulse, bassdrone, organ);
		Figure bf2 = f(pulse, bassdrone2, organ);
		Figure bf3 = f(pulse, bassdrone3, organ);
		
		Figure omf1 = f(pulse, ominous1, b);
		Figure omf2 = f(pulse, ominous2, b);
		Figure omf3 = f(pulse, ominous3, b);
		
		Figure l1f = f(pulse, lead1, oohs);
		Figure l2f = f(pulse, lead2, oohs);
		Figure l3f = f(lead3r, lead3, oohs);
		
		Figure l4f = f(lead4r, lead4, oohs);
		Figure l5f = f(pulse, lead5, oohs);
		Figure l6f = f(linger, lead6, oohs);
		
		add(lf1);
		add(lf2);
		add(lf3);

		add(lf1, bf);
		add(lf2, bf);
		add(lf3, bf);

		rpt(2, () ->
			add(lf1, bf, omf1).
			add(lf2, bf, omf2).
			add(lf3, bf, omf3));
		
		rpt(2, () -> add(bf));

		rpt(2, () -> 
			add(bf, l1f).
			add(bf, l2f).
			add(bf, l3f));

		// Need something else here!
		
		add(bf2, l4f);
		add(bf3, l5f);
		add(bf, l6f);
	}
	
	public void play() throws MidiUnavailableException {
		Player player = new Player();
		player.setComposition(getComposition());
		player.play();
	}
	
	public static void main(String[] args) throws MidiUnavailableException {
		Demo d = new Demo();
		d.create();
		d.play();
	}
}
