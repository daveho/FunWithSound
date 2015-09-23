package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.Rhythm;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

public class Demo8 extends DemoBase {
	@Override
	public void create() {
		tempo(220, 8);
		
		Instrument tr808 = percussion(TR808);
		Instrument tr909 = percussion(TR909);
		
		Instrument d = percussion(HS_VDW);// instr(DemoBase.HS_VDW, 21);
		Instrument d2 = instr(HS_VDW, 4);// instr(DemoBase.HS_VDW, 21);
		
		Rhythm tick1r = rr(p(0,80), 1, 8);
		Figure tick1f = pf(tick1r, 42, tr808);
		
		Rhythm kick1r = r(p(0,127), p(1,110), p(2,127), p(5, 127), p(6.5, 110), p(7.5, 127));
		Melody kick1m = m(an(25), an(24), an(26), an(26), an(26), an(24));
		Figure kick1f = f(kick1r, kick1m, d);
		
		Rhythm kick2r = r(p(0,127), p(1,110), p(2,127), p(5, 127), p(6.5, 110), p(7.5, 127));
		Melody kick2m = m(an(25), an(24), an(26), an(26), an(26), an(26));
		Figure kick2f = f(kick2r, kick2m, d);
		
		Rhythm hihatr = gr( rr(p(.5,127), .125, 4), rr(p(1,127), .25, 4), rr(p(2,127), .5,4), r(s(4,1.5,127), s(5.5,2,127)) );
		Melody hihatm = gm( rm(an(42), 12), m(an(49),an(49)) );
		System.out.printf("There are %d chords\n", hihatm.size());
		Figure hihatf = f(hihatr, hihatm, tr808);
		
		Rhythm bass1r = r(
				s(0.000,0.5,102), s(0.5,1.5,90), s(2,0.5,106),
				s(5,0.25,99), s(5.5,0.5,102), s(6.5,0.5,99), s(7,0.5,106));
		Melody bass1m = m(
				an(38), an(40), an(40),
				an(45), an(43), an(45), an(43));
		Figure bass1f = f(bass1r, bass1m, d2);
		
//		add1(gf(kick1f,hihatf));
//		add1(gf(kick2f,hihatf));
		add1(gf(kick1f,hihatf));
		add1(gf(kick2f,hihatf,bass1f));
		add1(gf(kick1f,hihatf,bass1f));
		add1(gf(kick2f,hihatf,bass1f));
		
		audition(d2);
	}
	
	public static void main(String[] args) throws IOException, MidiUnavailableException {
		Demo8 demo = new Demo8();
		demo.create();
		demo.play();
	}
}
