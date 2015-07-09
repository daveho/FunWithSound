package edu.ycp.cs.funwithsound;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A scale is an ascending sequence of midi pitches.
 * A typical heptatonic scale will have 7 pitches.
 * 
 */
public class Scale implements Iterable<Integer> {
	// See:  http://www.guitarland.com/Music10/FGA/LectureMIDIscales.html
	private static int[] MAJOR_INTERVALS = {2, 2, 1, 2, 2, 2, 1};
	private static int[] NATURAL_MINOR_INTERVALS = {2, 1, 2, 2, 1, 2, 2};
	private static int[] HARMONIC_MINOR_INTERVALS = {2, 1, 2, 2, 1, 3, 1};
	private static int[] MELODIC_MINOR_INTERVALS = {2, 1, 2, 2, 2, 2, 1};
	
	private List<Integer> pitches;
	
	public Scale() {
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
	
	public static Scale major(int start) {
		return makeScale(start, MAJOR_INTERVALS);
	}

	private static Scale makeScale(int start, int[] intervals) {
		Scale s = new Scale();
		int note = start;
		s.add(note);
		for (int add : intervals) {
			note += add;
			s.add(note);
		}
		return s;
	}
}
