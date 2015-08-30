package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.Rhythm;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

public class Demo5 extends DemoBase {
	private static final String SPDIR = "/home/dhovemey/Samples";

	@Override
	public void create() {
		tempo(210, 8);
		
		Instrument sp = samplePlayer();
		sp.addSample(0, SPDIR + "/torvalds/torvalds-says-linux.wav");
		sp.addSample(1, SPDIR + "/torvalds/torvalds-says-linux.wav", 3162, 3600);
		v(sp, 0.4);

		//Instrument drumkit = percussion(TR808);
		
		Instrument tr909 = percussion(TR909);

		/*
		Rhythm chihatr = rr(p(0, 101), .5, 4);
		Rhythm hihatr = gr(chihatr, r(s(2, 1.2, 101)), sr(4, chihatr), r(s(6, 1.2, 101)));
		Melody hihatm = m(an(42),an(42),an(42),an(42),an(46),an(42),an(42),an(42),an(42),an(46));
		Figure hihatf = f(hihatr, hihatm, drumkit);
		
		Rhythm kicksr = r(p(0, 101), p(3.5, 88), p(4, 101));
		Melody kicksm = m(an(36), an(63), an(36));
		Figure kicksf = f(kicksr, kicksm, drumkit);
		*/
//		Rhythm hihatr = rr(p(0, 101), 1, 8);
//		Figure hihatf = pf(hihatr, 42, drumkit);
//		Rhythm kicksr = rr(p(0, 101), 4, 2);
//		Figure kicksf = pf(kicksr, 36, drumkit);
		
		Rhythm percr = r(
				s(0.000,0.394,110), s(1,0.391,118), s(2,0.638,118), s(4,0.399,118), s(5,0.389,110), s(6,0.477,118));
		Melody percm = m(
				an(36), an(36), an(40), an(36), an(36), an(40));
		Figure percf = f(percr, percm, tr909);
		
		Rhythm ltr = r(p(0));
		Melody ltm = m(an(0));
		Figure ltf = f(ltr, ltm, sp);

		Rhythm llr = rr(p(0), .5, 5);
		Melody llm = m(an(1),an(1),an(1),an(1),an(1));
		Figure llf = f(llr, llm, sp);

		int where = m();
		
		add1n(10, percf);
		at(where+2, ltf);
		at(where+4, llf);
		at(where+5, llf);
		at(where+6, ltf);
		at(where+8, llf);
		at(where+9, llf);
		
//		audition(accent);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo5 demo = new Demo5();
		demo.create();
		demo.play();
	}

}
