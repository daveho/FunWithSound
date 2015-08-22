package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.Rhythm;
import io.github.daveho.funwithsound.Strike;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

public class Demo3 extends DemoBase {
	@Override
	public void create() {
		tempo(200, 8);
		major(51);

		/*
		Instrument met_kicks = percussion(TR808);
		Instrument met_hihat = percussion(TR808);
		*/
		Instrument drumkit = percussion(TR909);
		Instrument drumkit_hihats = percussion(TR808);
		Instrument accent_perc = percussion(ARACHNO);

		// This is basically just a metronome, don't need it now that there is a proper
		// underlying rhythm
		/*
		Strike kick = s(0, 40);
		Rhythm met_kickr = rr(kick, 4, 2);
		Figure met_kickf = pf(met_kickr, 36, met_kicks);
		Strike tap = s(0, 80);
		Rhythm met_hihatr = rr(tap, 1, 8);
		Figure met_hihatf = pf(met_hihatr, 42, met_hihat);
		*/

		// This is weird but cool
		Rhythm kicksr = r(
				s(0.000,1.909,127), s(2,0.583,118), s(2.5,0.447,118), s(3.5,2.126,118), s(6,0.712,118), s(6.5,0.559,118), s(7,1.230,118));
		Melody kicksm = m(
			an(36), an(38), an(36), an(36), an(38), an(36), an(36));
		Figure kicksf = f(kicksr, kicksm, drumkit);
		
		// Hihats (goes for 4 measures)
		Rhythm hihatr = r(
				s(0.000,0.401,99), s(0.928,0.536,118), s(4.051,0.413,118), s(5.615,0.465,110), s(5.956,2.072,78), s(7.929,0.432,110), s(8.991,0.387,110), s(11.915,0.184,118), s(12.346,0.278,87), s(13.630,0.526,110), s(14.024,2.155,90), s(15.978,0.421,118), s(17.097,0.402,118), s(20.034,0.286,110), s(21.703,0.464,118), s(22.034,2.041,93), s(23.854,0.430,110), s(24.943,0.156,110), s(25.380,0.390,106), s(27.913,0.255,118), s(28.501,0.345,110), s(29.681,0.504,118), s(30.079,2.484,99));
		Melody hihatm = m(
			an(42), an(42), an(42), an(42), an(46), an(42), an(42), an(42), an(42), an(42), an(46), an(42), an(42), an(42), an(42), an(46), an(42), an(42), an(42), an(42), an(42), an(42), an(46));
		Figure hihatf = f(hihatr, hihatm, drumkit_hihats);


		//add1n(16, gf(met_kickf, met_hihatf));
		add1n(16, kicksf);
		at(2, hihatf);
		at(6, hihatf);
		at(10, hihatf);
		
		audition(accent_perc);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo3 demo = new Demo3();
		demo.create();
		demo.play();
	}
}
