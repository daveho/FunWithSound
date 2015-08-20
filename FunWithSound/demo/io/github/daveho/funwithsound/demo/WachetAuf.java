package io.github.daveho.funwithsound.demo;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import io.github.daveho.funwithsound.*;

public class WachetAuf extends Composer {
	public void create() {
		tempo(200, 8);
		major(51); //  E flat major

		Instrument organ = instr(20);

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
