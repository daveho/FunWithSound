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
		setTempo(new Tempo(250, 8));
		
		Instrument oohs = new Instrument(54);
		Instrument organ = new Instrument(20);
		Instrument b = new Instrument(77);
		Instrument g = new Instrument(52);

		Rhythm leadin = r(fs(0), qs(1), fs(2), fs(3), fs(4), qs(5), fs(6), qs(7));
		Melody low = m(-7, -6, n(-7, -5), -4, n(-5, -3), n(-6, -2), n(-6, -1), n(-5, 0));
		Melody mid = xo(1, low);
		Melody hi = xo(2, low);
		
		Rhythm pulse = r(s(0,4), s(4,4));
		Melody bassdrone = xo(-3, m(0, 0));
		
		Melody ominous1 = xo(-2, m(n(0, 2), n(1, 3)));
		Melody ominous2 = xo(-2, m(0, n(2, 4)));
		Melody ominous3 = xo(-2, m(n(2, 5), n(1, 6)));
		
		Melody lead1 = m(2, 0);
		Melody lead2 = m(n(0, -3, -7));
		Melody lead3 = m(n(-3, 3), 0, 4, 0, 3, 1, n(3, -3), n(2, -1));
		Rhythm lead3r = r(fs(0), fs(1), fs(2), fs(3), fs(4), fs(5), fs(6), fs(7));

		Figure lf1 = f(leadin, low, oohs);
		Figure lf2 = f(leadin, mid, oohs);
		Figure lf3 = f(leadin, hi, oohs);
		
		Figure bf = f(pulse, bassdrone, organ);
		
		add(lf1);
		add(lf2);
		add(lf3);

		add(lf1);
		add(lf2);
		add(lf3);
		
//		at(0, pulse, lead1, g);
//		at(1, pulse, lead2, g);
//		at(2, lead3r, lead3, g);

//		for (int i = 0; i < 4; i++) {
//			at(i*3+0, leadin, low, oohs);
//			at(i*3+1, leadin, mid, oohs);
//			at(i*3+2, leadin, hi, oohs);
//		}
//		
//		for (int i = 1; i < 7; i++) {
//			at(i*3+0, pulse, bassdrone, organ);
//			at(i*3+1, pulse, bassdrone, organ);
//			at(i*3+2, pulse, bassdrone, organ);
//		}
//		
//		for (int i = 2; i < 4; i++) {
//			at(i*3+0, pulse, ominous1, b);
//			at(i*3+1, pulse, ominous2, b);
//			at(i*3+2, pulse, ominous3, b);
//		}
//		
//		for (int i = 5; i < 7; i++) {
//			at(i*3+0, pulse, lead1, g);
//			at(i*3+1, pulse, lead2, g);
//			at(i*3+2, lead3r, lead3, g);
//		}
//		
//		for (int i = 6; i < 7; i++) {
//			
//		}
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
