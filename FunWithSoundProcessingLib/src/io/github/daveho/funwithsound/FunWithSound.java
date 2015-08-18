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
		
		parent.registerMethod("dispose", this);
		
		System.out.println("Starting ##library.name## version ##library.prettyVersion##");
	}
	
	public void dispose() {
		player.stopPlaying();
	}
	
	public void play(Composer c) {
		player.setComposition(c.getComposition());
		player.startPlaying();
	}
}