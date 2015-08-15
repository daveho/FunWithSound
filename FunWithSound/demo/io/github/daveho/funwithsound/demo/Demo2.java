package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.Rhythm;
import io.github.daveho.funwithsound.Scale;
import io.github.daveho.funwithsound.Tempo;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

public class Demo2 extends DemoBase {
	public void create() {
		setScale(Scale.melodicMinor(65));
		setTempo(new Tempo(200, 8));

		Instrument tr808 = percussion(TR808);
		Instrument tr808_kicks = percussion(TR808);
		Instrument bass = instr(ARACHNO, 38);
		
		Rhythm kicks = r(p(0, 120), p(4));
		Figure kicksf = pf(kicks, 35, tr808_kicks);
		
		Rhythm basichihatr = rr(p(0, 88), 1, 8);
		Figure basichihatf = pf(basichihatr, 42, tr808);
		
		Rhythm bassr = r(
				s(0.000,2.365,79), s(2.1,0.709,101), s(3.5,0.7,110), s(4.45,0.184,118), s(4.95,0.375,110), s(5.95,1.040,110), s(6.95,0.873,106));
		Melody bassm = m(
				an(29), an(34), an(32), an(32), an(32), an(32), an(34));
		Figure bassf = f(bassr, bassm, bass);

		add1(kicksf);
		add1(kicksf);
		add1(kicksf, basichihatf);
		add1(kicksf, basichihatf);
		rpt(8, () ->
			add1(kicksf, basichihatf, bassf));
		
		//audition(bass);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo2 d = new Demo2();
		d.create();
		d.play();
	}
}
