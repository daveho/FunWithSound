// FunWithSound - A Java/Processing library for music composition
// Copyright 2015, David Hovemeyer <david.hovemeyer@gmail.com>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package io.github.daveho.funwithsound.demo;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import io.github.daveho.funwithsound.Composer;
import io.github.daveho.funwithsound.Player;
import io.github.daveho.funwithsound.Rhythm;
import io.github.daveho.funwithsound.Strike;

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
	
	// http://www.hammersound.com/cgi-bin/soundlink.pl?action=view_download_page;ID=673;SoundFont_Location_Selected=Download%20USA;SoundFont_Filename_Selected=PerKristianRisvik_Bandpass.zip
	public static final String BANDPASS = SOUNDFONT_DIR+"/bandpass/Bandpass.sf2";
	
	// http://www.hammersound.com/cgi-bin/soundlink.pl?action=view_download_page;ID=566;SoundFont_Location_Selected=Download%20USA;SoundFont_Filename_Selected=analog_age_set.rar
	public static final String ANALOG_AGE = SOUNDFONT_DIR+"/analogage/Analog Age_set.sf2";
	
	// http://www.hammersound.com/cgi-bin/soundlink_download2.pl/Download%20Norway;hs_tb303.zip;20
	public static final String TB303 = SOUNDFONT_DIR+"/tb303/HS TB-303.SF2";
	
	// http://www.hammersound.com/cgi-bin/soundlink_download2.pl/Download%20Norway;hs_pt1.zip;22
	public static final String HS_PT1 = SOUNDFONT_DIR + "/hammersound/HS Pads and Textures I.sf2";
	
	// http://www.hammersound.com/cgi-bin/soundlink_download2.pl/Download%20Norway;hs_pt2.zip;30
	public static final String HS_PT2 = SOUNDFONT_DIR + "/hammersound/HS Pads and Textures II.sf2";
	
	// http://www.hammersound.com/cgi-bin/soundlink_download2.pl/Download%20Norway;tr808.zip;17
	public static final String HS_TR808 = SOUNDFONT_DIR + "/hammersound/TR-808 Drums.SF2";
	
	// http://www.hammersound.com/cgi-bin/soundlink_download2.pl/Download%20Norway;hs_synt1.zip;21
	public static final String HS_SYNTH1 = SOUNDFONT_DIR + "/hammersound/HS Synth Collection I.sf2";
	
	// http://www.hammersound.com/cgi-bin/soundlink_download2.pl/Download%20Norway;hs_se.zip;63
	public static final String HS_SE = SOUNDFONT_DIR + "/hammersound/HS Synthetic Electronic.sf2";
	
	// http://www.hammersound.com/cgi-bin/soundlink_download2.pl/Download%20Norway;iw_vint.zip;8
	public static final String HS_VDW = SOUNDFONT_DIR + "/hammersound/Vintage Dreams Waves v2.sf2";
	
	public static final String GORT = SOUNDFONT_DIR + "/hammersound/Gort's-DoubleDecker_J1.SF2";
	
	public static final String RTANBAS = SOUNDFONT_DIR + "/hammersound/rtanbas1.SF2";
	
	public static final String BAZZ = SOUNDFONT_DIR + "/hammersound/Bazz.SF2";
	
	private String outputFile;
	
	public abstract void create();
	
	public Rhythm dr(double c, Rhythm r) {
		Rhythm result = new Rhythm();
		
		for (Strike s : r) {
			Strike ds = new Strike((long)(s.getStartUs()*c), (long)(s.getDurationUs()*c), s.getVelocity());
			result.add(ds);
		}
		
		return result;
	}
	
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	
	public void demo() throws MidiUnavailableException, IOException {
		create();
		play();
	}

	public void play() throws MidiUnavailableException, IOException {
		Player player = new Player();
		onCreatePlayer(player);
		player.setComposition(getComposition());
		if (outputFile != null) {
			player.setOutputFile(outputFile);
		}
		player.play();
	}

	protected void onCreatePlayer(Player player) {
	}
}
