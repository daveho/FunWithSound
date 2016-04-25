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

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import io.github.daveho.funwithsound.*;

public class WachetAuf extends Composer {
	private static final String SOUNDFONTS = "/home/dhovemey/SoundFonts";
	private static final String FLUID = SOUNDFONTS + "/fluid/FluidR3 GM2-2.SF2";
	
	public void create() {
		tempo(200, 8);
		major(51); //  E flat major

		Instrument organ = instr(FLUID, 20);

		Rhythm tr = r(
				s(0,2), s(2,1), s(3,1), s(4,1),
				s(6, 2), s(8,2), s(10,2), s(12,2), s(14, 2), s(16,2),
				s(18,1), s(19,1), s(20,1), s(22,2), s(24,2),
				s(26,2), s(28,2)
		);
		Melody tm = m(
				4, 7, 8, 9,
				9, 8, 10, 9, 4, 3,
				9, 7, 8, 3, 2,
				6, 7
		);
		Figure tf = f(tr, tm, organ);

		add1(tf);
	}

	public static void main(String[] args) throws MidiUnavailableException, IOException {
		WachetAuf demo = new WachetAuf();
		demo.create();
		Player player = new Player();
		player.setComposition(demo.getComposition());
		player.play();
	}
}
