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
		tempo(220, 8);
		
		Instrument sp = samplePlayer();
		sp.addSample(0, SPDIR + "/torvalds/torvalds-says-linux.wav");
		sp.addSample(1, SPDIR + "/torvalds/torvalds-says-linux.wav", 3162, 3700);
		v(sp, 0.4);

		Instrument drumkit = percussion(TR808);
		
		Rhythm chihatr = rr(p(0, 101), .5, 4);
		Rhythm hihatr = gr(chihatr, r(s(2, 1.2, 101)), sr(4, chihatr), r(s(6, 1.2, 101)));
		Melody hihatm = m(an(42),an(42),an(42),an(42),an(46),an(42),an(42),an(42),an(42),an(46));
		Figure hihatf = f(hihatr, hihatm, drumkit);
		
		Rhythm ltr = r(p(0));
		Melody ltm = m(an(0));
		Figure ltf = f(ltr, ltm, sp);

		Rhythm llr = rr(p(0), .25, 9);
		Melody llm = m(an(1),an(1),an(1),an(1),an(1),an(1),an(1),an(1),an(1));
		Figure llf = f(llr, llm, sp);
		
		add1n(8, hihatf);
		at(2, ltf);
		at(4, ltf);
		at(6, llf);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo5 demo = new Demo5();
		demo.create();
		demo.play();
	}

}
