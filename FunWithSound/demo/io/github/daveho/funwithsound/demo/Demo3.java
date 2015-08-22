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
		Instrument synth = instr(MINIMOOG, 3);

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
		
		// Another bass drum pattern
		Rhythm kick2r = r(s(0,1.9,127), s(3,.9, 95), s(4,.9,127));
		Figure kick2f = pf(kick2r, 36, drumkit);
		
		Rhythm yelpr = r(
				s(0.000,0.257,110), s(4.136,0.211,118), s(8.133,0.291,118), s(11.934,0.292,118), s(16.155,0.291,127), s(20.059,0.312,118), s(24.149,0.245,118), s(27.959,0.172,127));
		Melody yelpm = m(
				an(78), an(78), an(78), an(78), an(78), an(78), an(78), an(78));
		Figure yelpf = f(yelpr, yelpm, accent_perc);
		
		Rhythm acc1r = r(
				s(0.000,0.540,118), s(0.869,0.383,96), s(1.309,0.413,106), s(2.853,0.381,118), s(4.835,1.164,106), s(7.995,0.536,106), s(8.973,0.414,99), s(9.444,0.438,110), s(10.934,0.459,118), s(12.964,1.001,102), s(16.097,0.493,106), s(16.869,0.421,93), s(17.389,0.382,106), s(18.885,0.391,110), s(20.915,0.894,110), s(23.947,0.538,106), s(24.866,0.388,81), s(25.347,0.430,110), s(26.922,0.446,118), s(28.877,1.176,110));
		Melody acc1m = m(
				an(75), an(72), an(75), an(75), an(74), an(75), an(72), an(75), an(75), an(74), an(75), an(72), an(75), an(75), an(74), an(75), an(72), an(75), an(75), an(74));
		Figure acc1f = f(acc1r, acc1m, accent_perc);
		
		// Pow!
		Rhythm powr = r(
				s(0.000,16.046,110), s(16.013,7.876,106), s(23.881,4.025,90), s(27.846,4.201,96), s(32.039,15.972,110), s(47.977,8.115,110), s(56.016,3.941,102), s(59.873,4.225,102), s(64.109,32.071,106));
		Melody powm = m(
			an(64,71), an(65,72), an(67,74), an(69,76), an(64,71), an(65,72), an(67,74), an(69,76), an(62,69));
		Figure powf = f(powr, powm, synth);
		
		
		v(synth, 0.7);

		int here;

		//add1n(16, gf(met_kickf, met_hihatf));

		add1n(16, kicksf);
		at(2, hihatf);
		at(6, gf(hihatf, yelpf));
		at(10, gf(hihatf, yelpf, acc1f));
		
		add1n(4, kick2f);
		
		here = m();

		add1n(12, kicksf);
		at(here, gf(hihatf, yelpf, acc1f, powf)); // <-- POW!
		at(here+4, gf(hihatf, yelpf, acc1f));
		at(here+8, gf(hihatf, yelpf, acc1f));

		add1n(4, kick2f);

		audition(synth);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo3 demo = new Demo3();
		demo.create();
		demo.play();
	}
}