package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.Rhythm;
import io.github.daveho.funwithsound.Scale;
import io.github.daveho.funwithsound.Tempo;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

public class Demo2 extends DemoBase {
	public void create() {
		setScale(Scale.melodicMinor(65));
		setTempo(new Tempo(220, 8));

		Instrument drums = percussion();
		
		Rhythm thump = r(p(0), p(2), p(4), p(6));
		Melody thumpbass = m(pn(35), pn(35), pn(35), pn(35));
		
		Figure thumpf = f(thump, thumpbass, drums);
		
		add(thumpf);
		add(thumpf);
		add(thumpf);
		add(thumpf);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo2 d = new Demo2();
		d.create();
		d.play();
	}
}
