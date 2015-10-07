// FunWithSound - A Java/Processing library for music composition
// Copyright 2015, David Hovemeyer <david.hovemeyer@gmail.com>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.Rhythm;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

public class Demo3 extends DemoBase {
	@Override
	public void create() {
		tempo(200, 8);
		major(51);
		
		// NOTE: this composition sounds terrible without the
		// soundfonts.  See DemoBase for links to the soundfont files needed.
		
		Instrument drumkit = percussion(TR909);
		Instrument drumkit_hihats = percussion(TR808);
		Instrument accent_perc = percussion(ARACHNO);
		Instrument synth = instr(MINIMOOG, 3);
		Instrument lead = instr(ARACHNO, 95); // really cool lead sound
		
//		Instrument lead2 = instr(MINIMOOG, 7);

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
				s(0.000,0.257,110),
				//s(4.136,0.211,118),
				//s(8.133,0.291,118),
				//s(11.934,0.292,118),
				s(16.155,0.291,127)//,
				//s(20.059,0.312,118),
				//s(24.149,0.245,118)//,
				//s(27.959,0.172,127)
				);
		Melody yelpm = m(
				an(78), an(78)/*, an(78), an(78), an(78), an(78), an(78), an(78)*/);
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
		
		Rhythm over0r = r(
				s(0.000,2.066,96), s(1.982,2.937,93), s(4.798,1.008,96), s(5.905,7.104,102), s(12.838,0.914,96), s(13.999,7.239,110), s(21.168,0.701,110), s(21.948,3.917,102), s(25.782,4.563,106));
		Melody over0m = m(
				an(89), an(96), an(93), an(91), an(93), an(89), an(91), an(86), an(88));
		Figure over0f = f(sr(2, over0r), xm(-1, over0m), lead);
		
		// overlay bit
		Rhythm over1r = r(
				s(0.000,2.204,99), s(1.889,0.971,96), s(2.845,5.078,80), s(7.769,1.992,93), s(9.660,1.090,96), s(10.690,1.946,85), s(12.640,3.251,102), s(15.967,1.987,90), s(17.999,0.908,90), s(18.907,1.835,83), s(20.671,2.306,73), s(22.887,0.996,85), s(23.899,6.774,90), s(30.696,1.166,79), s(31.786,16.059,102));
		Melody over1m = m(
				an(64), an(65), an(62), an(64), an(65), an(67), an(65), an(64), an(65), an(64), an(62), an(60), an(62), an(60), an(62));
		Figure over1f = f(over1r, over1m, lead);
		
		Rhythm over2r = r(
				s(0.000,1.283,110), s(1.800,0.551,106), s(3.224,0.243,106), s(3.743,0.496,96), s(4.675,2.697,106), s(7.740,1.332,102), s(9.686,0.835,106), s(11.260,0.307,102), s(11.826,0.442,87), s(12.637,0.661,106), s(13.830,0.618,99), s(14.636,0.697,96), s(15.722,7.094,110), s(22.756,0.838,93), s(23.807,6.522,96), s(30.754,17.013,85), s(31.804,15.981,96));
		Melody over2m = m(
				an(79), an(79), an(79), an(79), an(77), an(79), an(79), an(79), an(79), an(77), an(74), an(77), an(76), an(77), an(74), an(74), an(81));
		Figure over2f = f(over2r, over2m, lead);
		
		v(synth, 0.5);

		int here;

		//add1n(16, gf(met_kickf, met_hihatf));
		add1n(16, kicksf);
		at(2, hihatf);
		at(6, gf(hihatf, yelpf));
		at(10, gf(hihatf, yelpf, acc1f));
		
		add1n(4, kick2f);
		
		here = currentMeasure();
		add1n(12, kicksf);
		at(here, gf(hihatf, yelpf, acc1f, powf)); // <-- POW!
		at(here+4, gf(hihatf, yelpf, acc1f));
		at(here+8, gf(hihatf, yelpf, acc1f));

		here = currentMeasure();
		add1n(12, kicksf);
		at(here, gf(hihatf, yelpf, acc1f, powf, over0f)); // <-- POW!
		at(here+4, gf(hihatf, yelpf, acc1f, over1f));
		at(here+8, gf(hihatf, yelpf, acc1f, over2f));

		add1n(6, kicksf);
		
//		audition(lead2);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo3 demo = new Demo3();
		demo.create();
//		demo.setOutputFile("demo3.wav");
		demo.play();
	}
}
