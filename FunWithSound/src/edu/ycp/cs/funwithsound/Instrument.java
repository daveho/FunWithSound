package edu.ycp.cs.funwithsound;

/**
 * An instrument plays notes/sounds as specified by
 * pairs of {@link Melody} and {@link Rhythm} objects.
 */
public class Instrument {
	private int patch;
	
	public Instrument(int patch) {
		this.patch = patch;
	}
	
	public int getPatch() {
		return patch;
	}
}
