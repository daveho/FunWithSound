package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.AddAutoPan;
import io.github.daveho.funwithsound.AddReverb;
import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.Rhythm;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

public class Demo5 extends DemoBase {
	@Override
	public void create() {
		tempo(200, 8);
		
		Instrument tr808 = percussion(HS_TR808);
//		v(tr808, 0.5);
		
		Instrument bleep = instr(GORT, 19); // 42
		//v(bleep, 0.6);
		addfx(bleep, new AddAutoPan(0.25, -.8, .8));
		//addfx(bleep, new AddReverb());
		
//		Instrument blorp = instr(ARACHNO, 69);
		Instrument blorp = instr(GORT, 3); // 42
		addfx(blorp, new AddAutoPan(0.5, -.8, .8));
		//addfx(blorp, new AddReverb());

//		Instrument bazz = instr(ARACHNO, 35);

		Instrument blurp = instr(ANALOG_AGE, 6);
		v(blurp, 0.4);

		Rhythm hihatr = rr(p(0,101),1,8);
		Figure hihatf = pf(hihatr, 42, tr808);
		Rhythm kickr = r(p(0,101));
		Figure kickf = pf(kickr, 36, tr808);
		
		Rhythm bleepr = rr(s(0,.5,101),.5,16);
		Melody bleepm = m(
				an(62), an(65), an(59), an(62),
				an(62), an(65), an(55), an(57),
				an(48), an(50), an(52), an(48),
				an(59), an(57), an(55), an(57));
		Figure bleepf = f(bleepr, bleepm, bleep);
		
		Melody bleep2m = m(
				an(72), an(74), an(76), an(69),
				an(72), an(74), an(76), an(65),
				an(67), an(65), an(67), an(64),
				an(72), an(74), an(76), an(67));
		Figure bleep2f = f(bleepr, bleep2m, bleep);
		
		Melody bleep3m = m(
				an(86), an(84), an(72), an(84),
				an(72), an(89), an(91), an(84),
				an(72), an(84), an(72), an(74),
				an(76), an(86), an(84), an(86));
		Figure bleep3f = f(bleepr, bleep3m, bleep);
		
		Melody bleep4m = m(
				an(96), an(72), an(86), an(89),
				an(76), an(79), an(81), an(60),
				an(74), an(53), an(52), an(67),
				an(65), an(53), an(57), an(59),
				an(48), an(81), an(77), an(62)
				/*, an(69), an(81), an(89), an(69), an(64), an(77), an(67), an(83)*/);
		Figure bleep4f = f(bleepr, bleep4m, bleep);
		
		Figure bleep1df = f(dilate(2, bleepr), bleepm, blorp);
		Figure bleep2df = f(dilate(2, bleepr), bleep2m, blorp);
		Figure bleep3df = f(dilate(2, bleepr), bleep3m, blorp);
		Figure bleep4df = f(dilate(2, bleepr), bleep4m, blorp);
		
//		Rhythm bass1r = r(
//				s(0.000,1,120), s(1,1,101), s(2.5,1.5,101), s(4,1,99), s(5,1.5,127));
//		Melody bass1m = m(
//				an(48), an(53), an(53), an(50), an(62));
//		Figure bass1f = f(sr(1,bass1r), xm(-1,bass1m), bazz);
		Rhythm bass1x = r(s(0,.5,101), s(0.5,.5,101), s(1.5,1.5,120));
		Rhythm bass1r = gr(bass1x, sr(4,bass1x));
		Melody bass1m = m(an(38),an(38),an(38),an(38),an(38),an(38));
		Figure bass1f = f(bass1r, bass1m, blurp);
		Rhythm bass2r = r(s(3,.5,101),s(3.5,.5,101),s(4,1,101),s(5,1,101),s(6,1,101));
		Melody bass2m = m(an(41), an(40), an(41), an(36), an(47));
		Figure bass2f = f(bass2r, bass2m, blurp);
		
//		add1(gf(hihatf,kickf,bass1f));
//		add1(gf(hihatf,kickf,bass2f));
//		add1(gf(hihatf,kickf,bass1f));
//		add1(gf(hihatf,kickf,bass2f));
		add1(gf(/*hihatf,kickf,bass1f,*/bleepf,bleep1df));
		add1(gf(/*hihatf,kickf,bass2f,*/bleep2f));
		add1(gf(/*hihatf,kickf,bass1f,*/bleep3f,bleep2df));
		add1(gf(/*hihatf,kickf,bass2f,*/bleep4f));
		add1(gf(/*hihatf,kickf,bass1f,*/bleepf, bleep3df));
		add1(gf(/*hihatf,kickf,bass2f,*/bleep2f));
		add1(gf(/*hihatf,kickf,bass1f,*/bleep3f, bleep4df));
		add1(gf(/*hihatf,kickf,bass2f,*/bleep4f));

		add1(gf(/*hihatf,kickf,bass1f,*/bleepf,bleep1df));
		add1(gf(/*hihatf,kickf,bass2f,*/bleep2f));
		add1(gf(/*hihatf,kickf,bass1f,*/bleep3f,bleep2df));
		add1(gf(/*hihatf,kickf,bass2f,*/bleep4f));
		add1(gf(/*hihatf,kickf,bass1f,*/bleepf, bleep3df));
		add1(gf(/*hihatf,kickf,bass2f,*/bleep2f));
		add1(gf(/*hihatf,kickf,bass1f,*/bleep3f, bleep4df));
		add1(gf(/*hihatf,kickf,bass2f,*/bleep4f));
		
//		add1(gf(hihatf,kickf));
//		add1(gf(hihatf,kickf));
//		add1(gf(hihatf,kickf));
//		add1(gf(hihatf,kickf));
		
		
		audition(blurp);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo5 demo = new Demo5();
		demo.create();
		demo.play();
	}
}
