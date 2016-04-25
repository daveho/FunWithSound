package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.Composer;
import io.github.daveho.funwithsound.Composition;
import io.github.daveho.funwithsound.Player;

public class Minimal extends Composer {
	static final String SOUNDFONTS = "/some/dir/SoundFonts";
	static final String FLUID = SOUNDFONTS + "/fluid/FluidR3 GM2-2.SF2";

	public void create() {
		// Composition goes here!
	}
	
	public static void main(String[] args) throws Exception {
		Minimal m = new Minimal();
		m.create();
		Composition c = m.getComposition();
		Player player = new Player();
		player.setComposition(c);
		player.play();
	}
}
