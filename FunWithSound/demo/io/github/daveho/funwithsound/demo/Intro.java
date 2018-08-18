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

import io.github.daveho.funwithsound.AddDelay;
import io.github.daveho.funwithsound.AddReverb;
import io.github.daveho.funwithsound.CustomInstrumentFactory;
import io.github.daveho.funwithsound.CustomInstrumentFactoryImpl;
import io.github.daveho.funwithsound.Defaults;
import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.MonoSynthUGen2;
import io.github.daveho.funwithsound.ParamNames;
import io.github.daveho.funwithsound.Player;
import io.github.daveho.funwithsound.RealizedInstrument;
import io.github.daveho.funwithsound.Rhythm;
import io.github.daveho.funwithsound.SynthToolkit;
import io.github.daveho.funwithsound.SynthToolkitBuilder;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.DataBead;

public class Intro extends DemoBase {
	public void create() {
		tempo(200, 8);  // 200 beats per minute, 8 beats per measure
		naturalMinor(57); // A minor, rooted at A3

		// Set up a percussion instruments for building a rhythm pattern
		Instrument drumkit = percussion(FLUID);

		// Nice synth bass sound
		Instrument bass = instr(VDW, 4);
		v(bass,0.55); // make the bass a bit quieter

		// Pads
		Instrument pad2 = instr(FLUID, 95); // Pad 1 (halo) 

		// Lead
		Instrument lead = custom(0);
		v(lead, 0.09); // the raw waveforms are super loud, quiet it down 
		addfx(lead, new AddDelay(300.0, 1.0, 0.5));
		addfx(lead, new AddDelay(600.0, 1.0, 0.4));
		addfx(lead, new AddDelay(900.0, 1.0, 0.3));
		addfx(lead, new AddReverb());

		Rhythm hihat4r = r(
				p(0,101), p(1,101), p(2,101), p(3,101),
				s(4,.5,101),
				p(4.5,101), s(5,.5,101), p(5.5,101),
				p(6.5,101), s(7,.5, 101), p(7.5,101));
		Melody hihat4m = m(
				an(42), an(42), an(42), an(42),
				an(46),
				an(44), an(46), an(44),
				an(44), an(46), an(44)
				);
		Figure hihat4f = f(hihat4r, hihat4m, drumkit);

		Rhythm pad3r = r(
				s(0,4,127), s(4,4,127), s(8,4,127),
				s(12,.5,127), s(12.5,.5,127), s(13,3,127)
				);
		Melody pad3m = m(
				an(45), an(36), an(45),
				an(48), an(50), an(52));
		Figure pad3f = f(pad3r, pad3m, pad2);

		Rhythm kick2r = rr(p(0,127), 2, 4);
		Figure kick2f = pf(kick2r, 36, drumkit);

		Rhythm snare2r = r(p(2,127), p(5,127), p(6.5,127));
		Melody snare2m = m(an(40), an(39), an(39));
		Figure snare2f = f(snare2r, snare2m, drumkit);

		Rhythm lead2r = r(
				s(0,2,99), s(2,6,106),
				s(7,1,90), s(8,1,106), s(9,7,101),
				s(16,2,77), s(18,5,106),
				s(23,1,76), s(24,1,65), s(25,7,79)
				);
		Melody lead2m = m(
				an(72), an(74),
				an(72), an(74), an(76),
				an(84), an(76),
				an(77), an(81), an(79));
		Figure lead2f = f(lead2r, lead2m, lead);

		Rhythm bass2r = r(
				s(0,1,110), s(1,1,102), s(2,1,85), s(3,.5,106), s(3.5,1,102), s(4.5,0.5,102), s(5,1,102), s(6,1,85), s(7,1,106),
				s(8,1,110), s(9,1,102), s(10,1,85), s(11,.5,106), s(11.5,1,102), s(12.5,0.5,102), s(13,1,102), s(14,1,85), s(15,1,106)
				);
		Melody bass2m = m(
				an(55), an(57), an(67), an(57), an(67), an(65), an(64), an(55), an(57),
				an(55), an(57), an(67), an(57), an(67), an(65), an(64), an(55), an(57));
		Figure bass2f = f(bass2r, bass2m, bass);
		
		Rhythm fastclapr = r(s(0,5), s(0.5,0.5), s(1,0.5), s(1.5,0.5), s(2,0.5));
		Figure fastclapf = pf(fastclapr, 39, drumkit);

		// The composition: which figures should start in each measure

		add1(gf(hihat4f,kick2f,snare2f,pad3f));
		add1(gf(hihat4f,kick2f,snare2f));
//		add1(gf(hihat4f,kick2f,snare2f,pad3f));
//		add1(gf(hihat4f,kick2f,snare2f));

//		add1(gf(hihat4f,kick2f,snare2f,pad3f,lead2f));
//		add1(gf(hihat4f,kick2f,snare2f));
//		add1(gf(hihat4f,kick2f,snare2f,pad3f));
//		add1(gf(hihat4f,kick2f,snare2f));
		add1(gf(hihat4f,kick2f,snare2f,pad3f,lead2f,bass2f));
		add1(gf(hihat4f,kick2f,snare2f));
		add1(gf(hihat4f,kick2f,snare2f,pad3f,bass2f));
		add1(gf(hihat4f,kick2f,snare2f));
		
		add1(gf(fastclapf));
	}

	@Override
	protected void onCreatePlayer(Player player) {
		CustomInstrumentFactory factory = new CustomInstrumentFactoryImpl(
				0, new CustomInstrumentFactoryImpl.CreateCustomInstrument() {
					public RealizedInstrument create(AudioContext ac) {
						DataBead params = Defaults.monosynthDefaults();
						params.put(ParamNames.GLIDE_TIME_MS, 80.0f);
						SynthToolkit tk = SynthToolkitBuilder.start()
								.withWaveVoice(Buffer.SAW)
								.withASRNoteEnvelope()
								.getTk();
						MonoSynthUGen2 u = new MonoSynthUGen2(ac, tk, params,
								new double[]{ 1.0, 1.5, 2.0 },
								new double[]{ 1.0, .5, .4 }
								);
						return new RealizedInstrument(u, ac);
					}
				});
		player.setCustomInstrumentFactory(factory);
	}

	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Intro d = new Intro();
		//d.demo();
		d.create();
		d.saveWaveFile("/home/dhovemey/IntroMusic.wav");
	}
}
