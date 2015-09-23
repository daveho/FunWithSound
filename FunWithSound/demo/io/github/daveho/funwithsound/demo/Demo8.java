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
		
		Instrument d = percussion(HS_VDW);
		
		Instrument d2 = percussion(HS_VDW);
		
		// Good bass sounds: 4, 20, 21
		Instrument bass = instr(HS_VDW, 21);
		v(bass, 0.5);
		
		Rhythm kick1r = r(p(0,127), p(1,110), p(2,127), p(5, 127), p(6.5, 110), p(7.5, 127));
		Melody kick1m = m(an(25), an(24), an(26), an(26), an(26), an(24));
		Figure kick1f = f(kick1r, kick1m, d);
		
		Rhythm kick2r = r(p(0,127), p(1,110), p(2,127), p(5, 127), p(6.5, 110), p(7.5, 127));
		Melody kick2m = m(an(25), an(24), an(26), an(26), an(26), an(26));
		Figure kick2f = f(kick2r, kick2m, d);
		
//		Rhythm hihatr = gr( rr(p(.5,127), .125, 4), rr(p(1,127), .25, 4), rr(p(2,127), .5,4), r(s(4,1.5,127), s(5.5,2,127)) );
//		Melody hihatm = gm( rm(an(42), 12), m(an(49),an(49)) );
		Rhythm hihatr = gr(rr(p(2,127), .5,4), r(s(4,1.5,127), s(5.5,2,127)) );
		Melody hihatm = gm( rm(an(42), 4), m(an(49),an(49)) );
		Figure hihatf = f(hihatr, hihatm, tr808);
		
		Rhythm accentr = r(p(5,105));
		Melody accent1m = m(an(56));
		Melody accent2m = m(an(54));
		Figure accent1f = f(accentr, accent1m, d2);
		Figure accent2f = f(accentr, accent2m, d2);
		
		Rhythm bass1r = r(
				s(0.000,0.5,102), s(0.5,1.5,90), s(2,0.5,106),
				s(5,0.5,99), s(5.5,0.5,102), s(7,0.5,99), s(7.5,0.5,106));
		Melody bass1m = m(
				an(38), an(40), an(40),
				an(45), an(43), an(45), an(43));
		Figure bass1f = f(bass1r, bass1m, bass);
		
		Rhythm bass2r = r(
				s(0.000,0.880,65), s(1.5,2,74), s(5,1,78), s(6,1,90));
		Melody bass2m = m(
				an(55), an(55), an(43), an(50));
		Figure bass2f = f(bass2r, bass2m, bass);

		Rhythm bass3r = sr(.5, r(
					s(0.000,1,73), s(2,1,80), s(3,.5,65), s(3.5,.5,96), s(4,1.5,79)));
		Melody bass3m = m(
				an(47), an(50), an(52), an(50), an(52));
		Figure bass3f = f(bass3r, bass3m, bass);
		
		Rhythm bass4r = r(s(0,1,102), s(1,.5,106), s(1.5,1,106));
		Melody bass4m = m(an(38), an(38), an(40));
		Figure bass4f = f(bass4r, bass4m, bass);

//		add1(gf(kick1f,hihatf));
//		add1(gf(kick2f,hihatf));
		add1(gf(kick1f,hihatf));
		add1(gf(kick1f,hihatf,bass1f));
		add1(gf(kick2f,hihatf,bass2f));
		add1(gf(kick1f,hihatf,bass1f));
		add1(gf(kick2f,hihatf,bass3f));
		add1(gf(kick1f,hihatf,bass4f,accent1f));
		add1(gf(kick1f,hihatf,accent2f));
		add1(gf(kick1f,hihatf,accent1f));
		add1(gf(kick1f,hihatf,accent2f));
		add1(gf(kick1f,hihatf,bass1f,accent1f));
		add1(gf(kick2f,hihatf,bass2f,accent2f));
		add1(gf(kick1f,hihatf,bass1f,accent1f));
		add1(gf(kick2f,hihatf,bass3f,accent2f));
		add1(gf(kick1f,bass4f,accent1f));
		add1(gf(kick2f,bass4f,accent2f));
		add1(gf(kick1f,bass4f,accent1f));
		add1(gf(kick2f,bass4f,accent2f));
		add1(gf(kick1f,hihatf,bass1f,accent1f));
		add1(gf(kick2f,hihatf,bass2f,accent2f));
		add1(gf(kick1f,hihatf,bass1f,accent1f));
		add1(gf(kick2f,hihatf,bass3f,accent2f));
		
		audition(d2);
	}
	
	public static void main(String[] args) throws IOException, MidiUnavailableException {
		Demo8 demo = new Demo8();
		demo.create();
		demo.play();
	}
}
