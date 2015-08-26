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


import processing.core.*;

/**
 * FunWithSound Processing library.
 * 
 * @author David Hovemeyer
 */
public class FunWithSound {
	PApplet parent;
	Player player;
	boolean playing;
	
	public FunWithSound(PApplet parent) {
		this.parent = parent;
		player = new Player();
		playing = false;
		
		parent.registerMethod("setup", this);
		parent.registerMethod("dispose", this);
		parent.registerMethod("draw", this);
		
		System.out.println("Starting ##library.name## version ##library.prettyVersion##");
	}
	
	public void dispose() {
		player.stopPlaying();
	}
	
	public void setup() {
		System.out.println("setup called?");
	}
	
	public void draw() {
		// Draw piano keyboard
		drawKeyboard();
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
				break;
			default:
				parent.fill(255);
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
				parent.fill(0);
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