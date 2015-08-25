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

import io.github.daveho.funwithsound.AddDelay;
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
		
		Instrument hh = percussion(TR808); // used for hihats
		v(hh, 0.5);
		
		Instrument sax = instr(ARACHNO, 66);
		AddReverb.Params params = AddReverb.defaultParams();
		params.roomSize = .95;
		addfx(sax, new AddReverb(params));
		
		Instrument bb = instr(ARACHNO, 99);
		addfx(bb, new AddDelay(375.0, 1.0, 0.7));
		addfx(bb, new AddDelay(750.0, 1.0, 0.7));
		v(bb, 0.5);
		
		Instrument bass = instr(ARACHNO, 34);
		addfx(bass, new AddReverb());
		v(bass, .5);
		
		Rhythm pr = r(
				s(0.000,1.984,118), s(2,0.646,118), s(2.826,0.248,118), s(3.281,2.587,118), s(6,0.892,118), s(6.830,0.250,127), s(7.270,2.773,106), s(10,0.742,118), s(11,0.253,118), s(11.382,0.710,99), s(13,0.657,118), s(14,0.492,118), s(14.387,0.808,118));
		Melody pm = m(
				an(36), an(40), an(36), an(36), an(40), an(36), an(36), an(40), an(36), an(36), an(36), an(40), an(36));
		Figure pf = f(pr, pm, drumkit2);
		
		Rhythm prh = gr(rr(p(0,88), .5, 4), rr(p(4,88), .5, 4));
		Figure prf = pf(prh, 42, hh);
		Rhythm prh2 = rr(s(0.000,1.166,88), 4, 2);
		Figure prf2 = pf(sr(2, prh2), 46, hh);
		
		Rhythm clickr = r(p(13, 110));
		Melody clickm = m(an(39));
		Figure clickf = f(clickr, clickm, drumkit);
		
		Rhythm saxr = r(
				s(0.000,2.023,110), s(2.023,4.009,102),
				s(6,0.930,79), s(7,0.723,90), s(7.363,0.501,99),
				s(7.895,2.023,79), s(10,4.111,102),
				s(14,0.863,81), s(15,0.657,71), s(15.4,0.572,75),
				s(16,2.285,78), s(18.053,3.940,96),
				s(22,1.003,99), s(23,0.560,79), s(23.4,0.495,102), s(24,6.881,75),
				
				s(31,0.825,99), s(32,2.1,99), s(34.05,4.1,85),
				s(38,0.824,73), s(39,0.623,77), s(39.45,0.516,99),
				s(40,2.002,68), s(42,4.173,83),
				s(46,0.902,93), s(47,0.631,69), s(47.4,0.509,93),
				s(48,2.009,72), s(50,4.067,96),
				s(54,1.097,90), s(55,0.551,80), s(55.4,0.501,102), s(56,7.515,90));
		Melody saxm = m(
				an(57), an(59), an(60), an(64), an(60), an(52), an(53), an(60), an(64), an(60), an(52), an(53), an(59), an(60), an(59), an(57), an(55), an(57), an(59), an(60), an(64), an(60), an(52), an(53), an(60), an(64), an(60), an(52), an(53), an(59), an(60), an(59), an(57));
		Figure saxf = f(saxr, saxm, sax);
		
		Rhythm bassr = r(
				s(0.000,0.476,110), s(1,0.182,106), s(1.4,6.187,96),
				s(8,0.765,118), s(9,0.494,72), s(9.35,1.549,110), s(11,5.230,106),
				s(16,0.856,106), s(17,0.485,61), s(17.4,1.630,106), s(18.9,4.190,93),
				s(23,0.496,106), s(24,0.380,106), s(24.4,0.492,110), s(25.5,5.161,110));
		Melody bassm = m(
				an(33), an(33), an(33),
				an(33), an(28), an(29), an(41),
				an(38), an(28), an(29), an(41),
				an(31), an(31), an(33), an(33));
		Figure bassf = f(sr(2, bassr), bassm, bass);
		
		int n = 26;

		// Kick/snare
		add1n(n, gf(prf, prf2));

		// Hihats, hand claps
		for (int i = 0; i < n; i += 2) {
			at(i, gf(pf,clickf));
		}

		// Bass comes in at measure 2
		for (int i = 2; i < n; i += 4) {
			at(i, bassf);
		}
		
		// Sax comes in at measure 6
		at(6, saxf);
		
		// Sax cuts out for 4 measures, then comes back in at measure 18
		at(18, saxf);
		
		//audition(bass);
	}

	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo4 demo = new Demo4();
		demo.create();
		demo.play();
	}
}
