package io.github.daveho.funwithsound.demo;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import io.github.daveho.funwithsound.Composer;
import io.github.daveho.funwithsound.Player;

public abstract class DemoBase extends Composer {
	private static final String SOUNDFONT_DIR = "/home/dhovemey/SoundFonts";

	// Some soundfonts
	
	// Arachno: http://www.arachnosoft.com/main/soundfont.php
	// This is a really excellent general soundfont for the standard
	// GM1 sound set.
	public static final String ARACHNO = SOUNDFONT_DIR+"/arachno/Arachno SoundFont - Version 1.0.sf2";
	
	// Sampled Korg M1 drum sounds, from http://www.hammersound.net/hs_soundfonts.html
	public static final String M1 = SOUNDFONT_DIR+"/m1/HS M1 Drums.sf2";
	
	// Roland TR-808 soundfont
	// From: http://www.suonoelettronico.com/soundfont_bank_download.asp
	public static final String TR808 = SOUNDFONT_DIR+"/tr808/Roland_TR-808_batteria_elettronica.sf2";

	// Roland TR-909 soundfont
	// From: http://www.suonoelettronico.com/soundfont_bank_download.asp
	public static final String TR909 = SOUNDFONT_DIR+"/tr909/Roland_TR-909_batteria_elettronica.sf2";
	
	// Minimoog soundfont
	// From: http://www.suonoelettronico.com/soundfont_scaricare_sf2.asp?nome=Minimoog%2053%20suoni
	public static final String MINIMOOG = SOUNDFONT_DIR+"/minimoog/minimoog_leads.sf2";
	
	private String outputFile;
	
	public abstract void create();
	
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	
	public void demo() throws MidiUnavailableException, IOException {
		create();
		play();
	}

	public void play() throws MidiUnavailableException, IOException {
		Player player = new Player();
		player.setComposition(getComposition());
		if (outputFile != null) {
			player.setOutputFile(outputFile);
		}else if (hasAudition()) {
			player.playLive(getAudition());
		}
		player.play();
	}
}
