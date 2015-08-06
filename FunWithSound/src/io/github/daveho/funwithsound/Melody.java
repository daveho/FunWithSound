package io.github.daveho.funwithsound;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A melody is a sequence of {@link Chord}s.
 * Note that melodies and chords do not include any timing
 * information: that is represented by {@link Rhythm}
 * and {@link Strike}.  A melody also does not specify
 * an instrument.
 */
public class Melody implements Iterable<Chord> {
	private List<Chord> chords;
	
	public Melody() {
		chords = new ArrayList<Chord>();
	}
	
	public void add(Chord chord) {
		chords.add(chord);
	}
	
	public int size() {
		return chords.size();
	}
	
	public Chord get(int index) {
		return chords.get(index);
	}
	
	@Override
	public Iterator<Chord> iterator() {
		return chords.iterator();
	}
}
