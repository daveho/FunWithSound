package io.github.daveho.funwithsound.demo;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Rhythm;
import io.github.daveho.funwithsound.Scale;
import io.github.daveho.funwithsound.Tempo;

public class Demo2 extends DemoBase {
	public void create() {
		setScale(Scale.melodicMinor(65));
		setTempo(new Tempo(220, 8));

		Instrument drums = percussion(TR808);
		
		Rhythm thump = rr(p(0), 2, 4);
		Figure thumpf = pf(thump, 36, drums);
		
		Rhythm hihat = rr(p(0), .5, 8);
		Figure hihatf = pf(hihat, 42, drums);
		
		Rhythm hihat2 = rr(p(0), .25, 8);
		Figure hihat2f = pf(hihat2, 42, drums);
		Rhythm hihat2b= rr(p(2), .5, 4);
		Figure hihat2bf = pf(hihat2b, 42, drums);
		Rhythm ride2 = r(p(6), p(7));
		Figure ride2f = pf(ride2, 46, drums);
		
		Rhythm snare1 = r(p(6));
		Rhythm snare2 = r(p(.5), p(6));
		Figure snare1f = pf(snare1, 37, drums);
		Figure snare2f = pf(snare2, 37, drums);
		
		add(thumpf);
		add(thumpf);
		add(thumpf, hihatf);
		add(thumpf, hihat2f, hihat2bf);
		add(thumpf, hihatf, ride2f, snare1f);
		add(thumpf, hihat2f, hihat2bf, ride2f, snare2f);
		add(thumpf, hihat2f, hihat2bf, ride2f, snare2f);
		add(thumpf, hihat2f, hihat2bf, ride2f, snare2f);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo2 d = new Demo2();
		d.create();
		d.play();
	}
}
