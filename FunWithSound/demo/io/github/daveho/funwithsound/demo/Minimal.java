package io.github.daveho.funwithsound.demo;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import io.github.daveho.funwithsound.*;

public class Minimal extends Composer {
	static final String SOUNDFONTS = "/home/dhovemey/SoundFonts";
	static final String FLUID = SOUNDFONTS + "/fluid/FluidR3 GM2-2.SF2";

	public void create() {
		// Composition goes here!
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Minimal m = new Minimal();
		m.create();
		Composition c = m.getComposition();
		Player player = new Player();
		player.setComposition(c);
		player.play();
	}
}
