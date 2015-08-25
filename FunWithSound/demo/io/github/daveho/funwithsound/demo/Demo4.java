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

import io.github.daveho.funwithsound.AddFlanger;
import io.github.daveho.funwithsound.AddFlanger.Params;
import io.github.daveho.funwithsound.AddReverb;
import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.Rhythm;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

public class Demo4 extends DemoBase {

	@Override
	public void create() {
		tempo(150, 8);
		major(51);
		
		Instrument drumkit = percussion(ARACHNO);
		
		Instrument drumkit2 = percussion(TR909);
		Instrument drumkit3 = percussion(TR808);
		
		Instrument synth = instr(ARACHNO, 95);
//		Params params = AddFlanger.defaultParams();
//		params.minDelayMs = 20.0;
//		params.maxDelayMs = 25.0;
//		params.h = 0.5;
//		addfx(synth, new AddFlanger(params));
		AddReverb.Params params = AddReverb.defaultParams();
		params.roomSize = .95;
		addfx(synth, new AddReverb(params));
		
		Rhythm pr = r(
				s(0.000,1.984,118), s(2,0.646,118), s(2.826,0.248,118), s(3.281,2.587,118), s(6,0.892,118), s(6.830,0.250,127), s(7.270,2.773,106), s(10,0.742,118), s(11,0.253,118), s(11.382,0.710,99), s(13,0.657,118), s(14,0.492,118), s(14.387,0.808,118));
		Melody pm = m(
				an(36), an(40), an(36), an(36), an(40), an(36), an(36), an(40), an(36), an(36), an(36), an(40), an(36));
		Figure pf = f(pr, pm, drumkit2);
		
		Rhythm prh = gr(rr(p(0,88), .5, 4), rr(p(4,88), .5, 4));
		Figure prf = pf(prh, 42, drumkit3);
		Rhythm prh2 = rr(s(0.000,1.166,88), 4, 2);
		Figure prf2 = pf(sr(2, prh2), 46, drumkit3);
		
		Rhythm clickr = r(p(13, 110));
		Melody clickm = m(an(39));
		Figure clickf = f(clickr, clickm, drumkit);
		
		add1n(8, gf(/*f,*/ gf(/*f2, */prf, prf2)));
		at(0, gf(pf,clickf));
		at(2, gf(pf,clickf));
		at(4, gf(pf,clickf));
		at(6, gf(pf,clickf));
		
		audition(synth);
	}

	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo4 demo = new Demo4();
		demo.create();
		demo.play();
	}
}
