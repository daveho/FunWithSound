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

import io.github.daveho.funwithsound.AddAutoPan;
import io.github.daveho.funwithsound.AddDelay;
import io.github.daveho.funwithsound.AddReverb;
import io.github.daveho.funwithsound.AddStaticPan;
import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.Rhythm;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

public class LinuxTechno extends DemoBase {
	private static final String SPDIR = "/home/dhovemey/Samples";

	@Override
	public void create() {
		tempo(210, 8);
		
		Instrument sp = samplePlayer();
		sp.addSample(0, SPDIR + "/torvalds/torvalds-says-linux.wav");
		sp.addSample(1, SPDIR + "/torvalds/torvalds-says-linux.wav", 3162, 3600); // Just "Linux"
		v(sp, 0.35);

		//Instrument drumkit = percussion(TR808);
		
		Instrument tr909 = percussion(TR909);
		
		Instrument conga = percussion(ARACHNO);
		addfx(conga, new AddDelay(200, 1, 0.8));
		addfx(conga, new AddDelay(400, 1, 0.75));
		addfx(conga, new AddDelay(600, 1, 0.7));
		addfx(conga, new AddDelay(800, 1, 0.65));
		addfx(conga, new AddAutoPan(.75, -.8, .8));
		
		Instrument bassint = instr(ANALOG_AGE, 10);
		addfx(bassint, new AddAutoPan(0.125, -.8, .8));
		addfx(bassint, new AddReverb());
		v(bassint, .8);
		
		//Instrument bass = instr(TB303, 9); // best: 5,9 good: 2,4,7,8  ok: 3 weird: 11,12,16(fun for break)
		Instrument bass = instr(ARACHNO, 39);
		v(bass, 0.5);
		
		Instrument bass2 = instr(ARACHNO, 39);
		//v(bass2, 0.6);
		
		Instrument bleep = instr(TB303, 16);
		v(bleep, 0.7);
		addfx(bleep, new AddStaticPan(-.8));
		
		Instrument bleep2 = instr(TB303, 11);
		v(bleep2, 0.8);
		addfx(bleep2, new AddStaticPan(.8));

		Rhythm percr = r(
				s(0.000,.8,110), s(1,.8,118), s(2,1.2,118), s(4,.4,118), s(4.5,.8,110), s(6,0.477,118));
		Melody percm = m(
				an(36), an(36), an(40), an(36), an(36), an(40));
		Figure percf = f(percr, percm, tr909);
		
		Rhythm bassintr = r(s(0.000,31.453,118), s(32,32.231,118));
		Melody bassintm = m(an(26), an(26));
		Figure bassintf = f(bassintr, bassintm, bassint);
		
		Rhythm bass1r = r(
				s(0.000,0.8,110), s(1,0.8,106), s(2,1.6,118), s(3,0.3,75), s(3.5,2,102),
				s(5,1,118), s(5.95,2,106));
		Melody bass1m = m(
				an(38), an(38), an(38), an(41), an(43), an(38), an(50));
		Figure bass1f = f(bass1r, xm(-1,bass1m), bass);
		
		Rhythm bass2r= r(
				s(0.000,0.476,102), s(1,0.447,118), s(2,0.434,118), s(2.5,0.665,118),
				s(3.5,0.663,118), s(5,1.764,127), s(7,.6,115));
		Melody bass2m = m(
				an(38), an(38), an(38), an(40), an(36), an(36), an(33));
		Figure bass2f = f(bass2r, bass2m, bass);
		
		Rhythm ltr = r(p(0));
		Melody ltm = m(an(0));
		Figure ltf = f(ltr, ltm, sp);

		Rhythm llr = rr(p(0), .5, 5);
		Melody llm = m(an(1),an(1),an(1),an(1),an(1));
		Figure llf = f(llr, llm, sp);
		
		Rhythm decayr = r(p(0, 127), p(2, 127), p(4, 110), p(6, 110), p(8, 100), p(10, 90), p(12, 80));
		Melody decaym = m(an(61),an(61),an(75),an(60),an(74),an(60),an(75));
		Figure decayf = f(decayr, decaym, conga);
		
		Rhythm bleep1r = r(
				s(0.000,1.795,118), s(2,0.9,110),
				s(3,0.416,110), s(3.400,1.523,102), s(5,0.940,118));
		Melody bleep1m = m(
				an(62), an(67),
				an(67), an(71), an(69));
		Figure bleep1f = f(bleep1r, bleep1m, bleep);
		
		Rhythm bleep2r = r(
				s(0.000,0.870,106), s(0.925,3.978,99), s(4.950,3.821,99), s(8.987,3.992,106),
				s(15.864,1.023,110), s(16.953,4.064,110), s(20.977,4.151,106), s(25.128,3.932,106));
		Melody bleep2m = m(
				an(62), an(72), an(71), an(69),
				an(62), an(71), an(69), an(67));
		Melody bleep3m = m(
				an(62), an(72), an(71), an(64),
				an(62), an(71), an(69), an(60));
		Figure bleep2f = f(sr(-1,bleep2r), bleep2m, bleep2);
		Figure bleep3f = f(sr(-1,bleep2r), bleep3m, bleep2);
		
/* 0*/	add1(gf(percf));
/* 1*/	add1(gf(percf));
/* 2*/	add1(gf(percf,bassintf,ltf));
/* 3*/	add1(gf(percf));
/* 4*/	add1(gf(percf, llf));
/* 5*/	add1(gf(percf, llf));
/* 6*/	add1(gf(percf,ltf));
/* 7*/	add1(gf(percf));
/* 8*/	add1(gf(percf,llf));
/* 9*/	add1(gf(percf,llf));
/*10*/	add1(gf(decayf));
/*11*/	add1(gf());
/*12*/	add1(gf(percf,bass1f));
/*13*/	add1(gf(percf,bass1f));
/*14*/	add1(gf(percf,bass1f,llf));
/*15*/	add1(gf(percf,bass2f,llf));
/*16*/	add1(gf(percf,bass1f,bassintf));
/*17*/	add1(gf(percf,bass1f));
/*18*/	add1(gf(percf,bass1f,llf));
/*19*/	add1(gf(percf,bass2f,llf));
/*20*/	add1(gf(percf,bass1f));
/*21*/	add1(gf(percf,bass1f));
/*22*/	add1(gf(percf,bass1f,llf));
/*23*/	add1(gf(percf,bass2f,llf));
/*24*/	add1(gf(percf,bass1f,bleep1f));
/*25*/	add1(gf(percf,bass1f,bleep2f));
/*26*/	add1(gf(percf,bass1f,bleep1f));
/*27*/	add1(gf(percf,bass2f));
/*28*/	add1(gf(percf,bass1f,bleep1f));
/*29*/	add1(gf(percf,bass1f,bleep3f));
/*30*/	add1(gf(percf,bass1f,bleep1f));
/*31*/	add1(gf(percf,bass2f));
/*32*/	add1(gf(percf,bass1f,llf));
/*33*/	add1(gf(percf,bass1f,llf));
/*34*/	add1(gf(percf,bass1f,llf));
/*35*/	add1(gf(percf,bass2f));
/*36*/	add1(gf(percf,bass1f,llf,bassintf));
/*37*/	add1(gf(percf,bass1f,llf));
/*38*/	add1(gf(percf,bass1f,llf));
/*39*/	add1(gf(percf,bass2f));

		audition(bass2);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		LinuxTechno demo = new LinuxTechno();
		demo.create();
//		demo.setOutputFile("linuxtechno.wav");
		demo.play();
	}

}