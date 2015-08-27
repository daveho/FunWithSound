/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is distributed under the terms of the
 * <a href="http://opensource.org/licenses/MIT">MIT license</a>.
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package io.github.daveho.funwithsound;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import processing.core.*;
import processing.event.KeyEvent;

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
	
	public FunWithSound(PApplet parent) {
		this.parent = parent;
		player = new Player();
		playing = false;
		startNote = 60;  // is always a C
		noteOn = new BitSet();
		
		parent.registerMethod("dispose", this);
		parent.registerMethod("draw", this);
		System.out.println("Hey ya");
		parent.registerMethod("keyEvent", this);
		System.out.println("Smooooooove");
		
		System.out.println("Starting ##library.name## version ##library.prettyVersion##");
	}
	
	public void dispose() {
		player.stopPlaying();
	}
	
	public void draw() {
		// Draw piano keyboard
		drawKeyboard();
	}

	// Map of key codes to offsets from current start note.
	//  e r   y u i    are the black keys
	// s d f g h j k l are the white keys
	private static final Map<Integer, Integer> NOTE_MAP = new HashMap<Integer,Integer>();
	static {
		NOTE_MAP.put(java.awt.event.KeyEvent.VK_S, 0);
		NOTE_MAP.put(java.awt.event.KeyEvent.VK_E, 1);
		NOTE_MAP.put(java.awt.event.KeyEvent.VK_D, 2);
		NOTE_MAP.put(java.awt.event.KeyEvent.VK_R, 3);
		NOTE_MAP.put(java.awt.event.KeyEvent.VK_F, 4);
		NOTE_MAP.put(java.awt.event.KeyEvent.VK_G, 5);
		NOTE_MAP.put(java.awt.event.KeyEvent.VK_Y, 6);
		NOTE_MAP.put(java.awt.event.KeyEvent.VK_H, 7);
		NOTE_MAP.put(java.awt.event.KeyEvent.VK_U, 8);
		NOTE_MAP.put(java.awt.event.KeyEvent.VK_J, 9);
		NOTE_MAP.put(java.awt.event.KeyEvent.VK_I, 10);
		NOTE_MAP.put(java.awt.event.KeyEvent.VK_K, 11);
		NOTE_MAP.put(java.awt.event.KeyEvent.VK_L, 12);
	}
	
	public void keyEvent(KeyEvent e) {
		//System.out.println("key event!!!");
		
		int action = e.getAction();
		if (action == KeyEvent.PRESS) {
			System.out.println("Press");
		} else if (action == KeyEvent.RELEASE) {
			System.out.println("Release");
		}
/*
		java.awt.event.KeyEvent e = (java.awt.event.KeyEvent) e_.getNative();
		
		boolean isPress = (e.getModifiers() & java.awt.event.KeyEvent.KEY_PRESSED) != 0;
		boolean isRelease = (e.getModifiers() & java.awt.event.KeyEvent.KEY_RELEASED) != 0;
		if (!(isPress || isRelease)) {
			return;
		}

		System.out.println("press or release");
		
		if (NOTE_MAP.containsKey(e.getKeyCode())) {
			int offset = NOTE_MAP.get(e.getKeyCode());
			if (isPress) {
				noteOn.set(startNote + offset);
			} else {
				noteOn.clear(startNote + offset);
			}
		}
*/
	}
	
	private static final int Y = 10;
	private static final int X_INIT = 10;
	private static final int W = 11;
	private static final int H = 60;
	
	private void drawKeyboard() {
		int cyc;
		int x;
		
		parent.stroke(0);
		parent.strokeWeight(1.0f);
		
		// Draw the white keys first
		cyc = 9;  // cyc%12==0 means a C, note 21 is an A (9 half steps above C)
		x = X_INIT;
		for (int note = 21; note <= 108; note++) {
			switch (cyc%12) {
			case 1: case 3: case 6: case 8: case 10:
				// Black key
				break;
			default:
				// White key
				if (noteOn.get(note)) {
					parent.fill(0,0,255);
				} else if (note >= startNote && note <= startNote+12) {
					// This note is in the active octave
					parent.fill(152,251,152);
				} else {
					parent.fill(255);
				}
				parent.rect(x, Y, W, H);
				x += W;
				break;
			}
			
			cyc++;
		}
		
		// Then draw the black keys
		cyc = 9;
		x = X_INIT;
		for (int note = 21; note <= 108; note++) {
			switch (cyc%12) {
			case 1: case 3: case 6: case 8: case 10:
				// Black key
				if (noteOn.get(note)) {
					parent.fill(0,0,255);
				} else {
					parent.fill(0);
				}
				parent.rect(x-((W/2)-2)-1, Y, W-2, (H*3)/5);
				break;
			default:
				// White key
				x += W;
				break;
			}
			
			cyc++;
		}
	}

	public void play(Composer c) {
		player.setComposition(c.getComposition());
		player.startPlaying();
	}
}