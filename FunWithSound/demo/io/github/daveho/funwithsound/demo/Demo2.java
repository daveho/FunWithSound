package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.Rhythm;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

public class Demo2 extends DemoBase {
	public void create() {
		melodicMinor(65);
		tempo(200, 8);

		Instrument drums = percussion(M1);
		Instrument drums_kicks = percussion(M1);
		Instrument bass = instr(ARACHNO, 38);
		Instrument newage = instr(ARACHNO, 88);
		Instrument lead = instr(ARACHNO, 11);
		
		Rhythm kicks = r(p(0, 120), p(4));
		Figure kicksf = pf(kicks, 35, drums_kicks);
		
		Rhythm basichihatr = rr(p(0, 88), 1, 8);
		Figure basichihatf = pf(basichihatr, 42, drums);
		
		Rhythm bassr = r(
				s(0.000,2.365,79), s(2.1,0.709,101), s(3.5,0.7,110), s(4.45,0.184,118), s(4.95,0.375,110), s(5.95,1.040,110), s(6.95,0.873,106));
		Melody bassm = m(
				an(29), an(34), an(32), an(32), an(32), an(32), an(34));
		Figure bassf = f(bassr, bassm, bass);
		
		Rhythm chimer = r(
				s(0.000,15.412,80), s(16,15.478,81), s(32,15.332,83), s(48,15.169,85));
		Melody chimem = m(
				an(60), an(60), an(60), an(60));
		Figure chimef = f(chimer, chimem, newage);
		
		Rhythm lead1r = r(
				s(0.000,5.932,72), s(0.045,6.100,105), s(7,0.981,91), s(8,2.008,100),
				s(10,0.891,91), s(10.984,1.809,100), s(13,2.693,83), s(15.994,7.290,91),
				s(16.032,7.467,96), s(24,1.888,78), s(26,0.806,83), s(27,1.856,78),
				s(29,2.813,74), s(32.034,6.295,78), s(32.107,6.375,66), s(39,0.923,83),
				s(40.064,2.037,91), s(42,0.726,74), s(43.066,1.946,74), s(45,2.909,105),
				s(48.060,6.329,100), s(55,1.067,58), s(56,7.867,96));
		Melody lead1m = m(
			an(72), an(65), an(65), an(67), an(68), an(67), an(65), an(72),
			an(65), an(72), an(70), an(68), an(70), an(65), an(72), an(65),
			an(67), an(68), an(70), an(68), an(65), an(63), an(65));
		Figure lead1f = f(lead1r, lead1m, lead);

		// This is the basic percussion and bass line
		add1(kicksf);
		add1(kicksf);
		add1(kicksf, basichihatf);
		add1(kicksf, basichihatf);
		v(bass, 0.6); // quiet the bass just a bit
		rpt(24, () ->
			add1(kicksf, basichihatf, bassf));
		
		// Chime-y sounds
		at(8, chimef);
		at(16, chimef);
		
		// Turn down the volume a bit on the chime-y part
		v(newage, 0.4);
		
		// Lead part
		at(16, lead1f);
		v(lead, 0.3);
		
//		audition(lead);
//		v(lead, 0.5);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo2 d = new Demo2();
		d.create();
		d.play();
	}
}
