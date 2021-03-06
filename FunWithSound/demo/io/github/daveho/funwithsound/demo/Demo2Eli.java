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

public class Demo2Eli extends DemoBase {
	public void create() {
		melodicMinor(65);
		tempo(200, 8);

		Instrument tr808 = percussion(M1);
		Instrument tr808_kicks = percussion(M1);
		Instrument bass = instr(ARACHNO, 31);
		Instrument lead = instr(ARACHNO, 84); // <--- change this number
		
		Rhythm kicks = r(p(5, 125), p(10));
		final Figure kicksf = pf(kicks, 30, tr808_kicks);
		
		Rhythm basichihatr = rr(p(10, 100), 1, 10);
		final Figure basichihatf = pf(basichihatr, 40, tr808);
		
		Rhythm bassr = r(
				s(0.000,2.365,79), s(2.1,0.709,101), s(3.5,0.7,110), s(4.45,0.184,118), s(4.95,0.375,110), s(5.95,1.040,110), s(6.95,0.873,106));
		Melody bassm = m(
				an(29), an(34), an(32), an(32), an(32), an(32), an(34));
		final Figure bassf = f(bassr, bassm, bass);
		
		Rhythm chimer = r(
				s(0.000,15.412,80), s(16,15.478,81), s(32,15.332,83), s(48,15.169,85));
		Melody chimem = m(
				an(60), an(60), an(60), an(60));
		Figure chimef = f(chimer, chimem, lead);

		// This is the basic percussion and bass line
		add1(kicksf);
		add1(kicksf);
		add1(gf(kicksf, basichihatf));
		add1(gf(kicksf, basichihatf));
		add1n(24, gf(kicksf, basichihatf, bassf));
		
		// Chime-y sounds
		at(8, chimef);
		at(16, chimef);
		
		audition(lead);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo2Eli d = new Demo2Eli();
		d.create();
		d.play();
	}
}
