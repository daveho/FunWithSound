package io.github.daveho.funwithsound.demo;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import io.github.daveho.funwithsound.Composer;
import io.github.daveho.funwithsound.Player;

public abstract class DemoBase extends Composer {
	// Some soundfonts
	
	// Arachno: http://www.arachnosoft.com/main/soundfont.php
	// This is a really excellent general soundfont for the standard
	// GM1 sound set.
	public static final String ARACHNO = "/home/dhovemey/SoundFonts/arachno/Arachno SoundFont - Version 1.0.sf2";
	
	// Sampled Korg M1 drum sounds, from http://www.hammersound.net/hs_soundfonts.html
	public static final String M1 = "/home/dhovemey/SoundFonts/m1/HS M1 Drums.sf2";
	
	public abstract void create();

	public void play() throws MidiUnavailableException, IOException {
		Player player = new Player();
		player.setComposition(getComposition());
		if (hasAudition()) {
			player.playLive(getAudition());
		}
		player.play();
	}
}