package io.github.daveho.funwithsound;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A chord is a collection of one or more midi pitches.
 * Note that most chords will actually represent a single
 * note, but it is useful to allow combinations of notes
 * to be represented (so that they are played together).
 * Note that a chord does not represent any start time or
 * duration: that is represented by a {@link Strike}.
 */
public class Chord implements Iterable<Integer> {
	private List<Integer> pitches;
	
	public Chord() {
		pitches = new ArrayList<Integer>();
	}
	
	public void add(int pitch) {
		pitches.add(pitch);
	}
	
	public int size() {
		return pitches.size();
	}
	
	public int get(int index) {
		return pitches.get(index);
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return pitches.iterator();
	}
}
