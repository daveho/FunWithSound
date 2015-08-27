/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is distributed under the terms of the
 * <a href="http://opensource.org/licenses/Apache-2.0">Apache License 2.0</a>.
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package io.github.daveho.funwithsound;

import io.github.daveho.gervill4beads.Midi;
import io.github.daveho.gervill4beads.ReceivedMidiMessageSource;

import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import processing.core.PApplet;

/**
 * FunWithSound Processing library.
 * 
 * @author David Hovemeyer
 */
public class FunWithSound {
	PApplet parent;
	Player player;
	boolean playing;
	int startNote; // which note corresponds to the leftmost keyboard key
	BitSet noteOn;
	ReceivedMidiMessageSource messageSource;
	
	public FunWithSound(PApplet parent) {
		this.parent = parent;
		player = new Player() {
			@Override
			protected void prepareToPlay() throws MidiUnavailableException, IOException {
				super.prepareToPlay();
				
				// Get the MidiMessageSource so we can send MidiMessages
				// to the Gervill instance playing the live audition part.
				messageSource = getMessageSource();
				if (messageSource != null) {
					System.out.println("Live audition!");
				} else {
					System.out.println("No live audition");
				}
			}
		};
		playing = false;
		startNote = 60;  // is always a C
		noteOn = new BitSet();
		
		parent.registerMethod("dispose", this);
		parent.registerMethod("draw", this);
		
		System.out.println("Starting ##library.name## version ##library.prettyVersion##");
	}

	public void dispose() {
		player.stopPlaying();
	}
	
	public void draw() {
		// Draw piano keyboard
		drawKeyboard();
	}

	// Map of keys to offsets from current start note.
	//  e r   y u i    are the black keys
	// s d f g h j k l are the white keys
	private static final Map<Character, Integer> OFFSET_MAP = new HashMap<Character,Integer>();
	static {
		OFFSET_MAP.put('s', 0);
		OFFSET_MAP.put('e', 1);
		OFFSET_MAP.put('d', 2);
		OFFSET_MAP.put('r', 3);
		OFFSET_MAP.put('f', 4);
		OFFSET_MAP.put('g', 5);
		OFFSET_MAP.put('y', 6);
		OFFSET_MAP.put('h', 7);
		OFFSET_MAP.put('u', 8);
		OFFSET_MAP.put('j', 9);
		OFFSET_MAP.put('i', 10);
		OFFSET_MAP.put('k', 11);
		OFFSET_MAP.put('l', 12);
	}
	
	public void onKeyPress(char key) {
		if (OFFSET_MAP.containsKey(key)) {
			int note = startNote+OFFSET_MAP.get(key);
			noteOn.set(note);
			if (messageSource != null) {
				ShortMessage msg = Midi.createShortMessage(ShortMessage.NOTE_ON, note, 127);
				messageSource.send(msg, -1L);
			}
		}
	}
	
	public void onKeyRelease(char key) {
		if (OFFSET_MAP.containsKey(key)) {
			int note = startNote+OFFSET_MAP.get(key);
			noteOn.clear(note);
			if (messageSource != null) {
				ShortMessage msg = Midi.createShortMessage(ShortMessage.NOTE_OFF, note);
				messageSource.send(msg, -1L);
			}
		}
	}
	
	private static final int Y = 10;
	private static final int X_INIT = 10;
	private static final int W = 11;
	private static final int H = 60;
	
	private static final int BLACK_KEYS = 0x54A; // bits indicate black keys
	
	private void drawKeyboard() {
		int cyc;
		int x;
		
		parent.stroke(0);
		parent.strokeWeight(1.0f);
		
		// Draw the white keys first
		cyc = 9;  // cyc%12==0 means a C, note 21 is an A (9 half steps above C)
		x = X_INIT;
		for (int note = 21; note <= 108; note++) {
			if (((1 << (cyc%12)) & BLACK_KEYS) == 0) {
				// White key
				if (noteOn.get(note)) {
					// Note is being played
					parent.fill(0,0,255);
				} else if (note >= startNote && note <= startNote+12) {
					// This note is in the active octave
					parent.fill(152,251,152);
				} else {
					parent.fill(255);
				}
				parent.rect(x, Y, W, H);
				x += W;
			}
			
			cyc++;
		}
		
		// Then draw the black keys
		cyc = 9;
		x = X_INIT;
		for (int note = 21; note <= 108; note++) {
			if (((1 << (cyc%12)) & BLACK_KEYS) != 0) {
				// Black key
				if (noteOn.get(note)) {
					// Note is being played
					parent.fill(0,0,255);
				} else {
					parent.fill(0);
				}
				parent.rect(x-((W/2)-2)-1, Y, W-2, (H*3)/5);
			} else {
				// White key
				x += W;
			}
			
			cyc++;
		}
	}

	public void play(Composer c) {
		player.setComposition(c.getComposition());
		if (c.hasAudition()) {
			player.playLive(c.getAudition());
		}
		player.startPlaying();
	}
}