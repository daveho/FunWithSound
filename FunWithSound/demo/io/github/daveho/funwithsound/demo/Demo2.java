package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Rhythm;
import io.github.daveho.funwithsound.Scale;
import io.github.daveho.funwithsound.Tempo;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

public class Demo2 extends DemoBase {
	public void create() {
		setScale(Scale.melodicMinor(65));
		setTempo(new Tempo(220, 8));

		Instrument tr808 = percussion(TR808);
		Instrument tr808_2 = percussion(TR808);
		Instrument synth = instr(ARACHNO);

		/*
		Rhythm hihat = rr(p(0), .5, 8);
		Figure hihatf = pf(hihat, 42, tr808);
		
		Rhythm hihat2 = rr(p(0), .25, 8);
		Figure hihat2f = pf(hihat2, 42, tr808);
		Rhythm hihat2b= rr(p(2), .5, 4);
		Figure hihat2bf = pf(hihat2b, 42, tr808);
		Rhythm ride2 = r(p(6), p(7));
		Figure ride2f = pf(ride2, 46, tr808);
		
		Rhythm snare1 = r(p(6), p(6.5), p(7));
		Rhythm snare2 = r(p(0), p(.5), p(6), p(6.5), p(7));
		Figure snare1f = pf(snare1, 75, tr808);
		Figure snare2f = pf(snare2, 75, tr808);
		
		add(hihatf, ride2f, snare1f);
		add(hihat2f, hihat2bf, ride2f, snare2f);
		add(hihat2f, hihat2bf, ride2f, snare2f);
		add(hihat2f, hihat2bf, ride2f, snare2f);
		*/
		
		Rhythm hihat1a = r(p(0), p(2), p(2.5), p(3), p(3.5));
		Figure hihat1af = pf(hihat1a, 42, tr808); 
		Rhythm hihat1b = rr(p(4), .25, 4);
		Figure hihat1bf = pf(hihat1b, 42, tr808);
		Rhythm ride1a = r(p(5), p(7));
		Figure ride1af = pf(ride1a, 46, tr808_2);
		Rhythm hihat1c = rr(p(6), .25, 4);
		Figure hihat1cf = pf(hihat1c, 42, tr808);
		
		add(hihat1af, hihat1bf, ride1af, hihat1cf);
		add(hihat1af, hihat1bf, ride1af, hihat1cf);
		add(hihat1af, hihat1bf, ride1af, hihat1cf);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo2 d = new Demo2();
		d.create();
		d.play();
	}
}
