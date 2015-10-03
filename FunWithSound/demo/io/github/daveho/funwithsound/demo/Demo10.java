package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.Rhythm;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

public class Demo10 extends DemoBase {

	@Override
	public void create() {
		tempo(220, 8);
		
		
		Instrument met = percussion(TR808);
		
		Instrument fluid = instr(FLUID, 104);
		Instrument flead = instr(FLUID, 86);

		Rhythm metr = rr(p(0,101), 2, 4);
		Figure metf = pf(metr, 42, met);
		
		Rhythm arpr = r(
				s(0.000,1,66), s(1,1.5,87), s(3,2,76), s(5,3.5,78),
				s(8,1,66), s(9,1.5,76), s(11,2,73), s(13,3.5,78));
		Melody arpm = m(
				an(48), an(52), an(59), an(62), an(48), an(52), an(60), an(64));
		Figure arpf = f(arpr, arpm, fluid);
		
		Rhythm ilr = r(
				s(0.000,1,71),
				s(1,1,99),
				s(2,1,43),
				s(3,1,67), 
				s(4,1,83),
				s(5,1,72),
				s(6,1,58),
				s(7,5,75));
		Melody ilm = m(
				an(65), an(67), an(79), an(76), an(74), an(72), an(65), an(67));
		Figure ilf = f(sr(1,ilr), ilm, flead);
		
		add1(gf(metf,arpf));
		add1(gf(metf));
		add1(gf(metf,arpf));
		add1(gf(metf));
		add1(gf(metf,arpf));
		add1(gf(metf));
		add1(gf(metf,ilf));
		
		audition(flead);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo10 demo = new Demo10();
		demo.create();
		demo.play();
	}
}
