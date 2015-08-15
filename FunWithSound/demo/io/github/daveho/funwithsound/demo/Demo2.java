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

		Instrument drums = percussion(Demo.SF);
		
		Rhythm thump = r(p(0), p(2), p(4), p(6));
		Figure thumpf = pf(thump, 51, drums);
		
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
