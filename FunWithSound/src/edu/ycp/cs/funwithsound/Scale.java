package edu.ycp.cs.funwithsound;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A scale is an ascending sequence of midi pitches.
 * A typical heptatonic scale will have 7 pitches.
 */
public class Scale implements Iterable<Integer> {
	// See:  http://www.guitarland.com/Music10/FGA/LectureMIDIscales.html
	// Note that we don't need the last value in the sequence,
	// since it produces the pitch of the beginning of the next
	// (higher) octave.  (Note how the sum of each sequence
	// is 12.)
	private static int[] MAJOR_INTERVALS = {2, 2, 1, 2, 2, 2/*, 1*/};
	private static int[] NATURAL_MINOR_INTERVALS = {2, 1, 2, 2, 1, 2/*, 2*/};
	private static int[] HARMONIC_MINOR_INTERVALS = {2, 1, 2, 2, 1, 3/*, 1*/};
	private static int[] MELODIC_MINOR_INTERVALS = {2, 1, 2, 2, 2, 2/*, 1*/};
	
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
	
	/**
	 * Get the pitch (midi note number) of the specified note
	 * in the scale.
	 * 
	 * @param index the note in the scale: should be in the range 0..6
	 *              for a standard heptatonic scale
	 * @return the pitch (midi note number)
	 */
	public int get(int index) {
		return pitches.get(index);
	}
	
	/**
	 * Get the pitch (midi note number) of the specified note
	 * in the scale, offset by given octave.
	 * 
	 * @param index the note in the scale: should be in the range 0..6
	 *              for a standard heptatonic scale
	 * @param octave the octave (0 for the original octave,
	 *               -1 for the next lower octave, 1 for the next
	 *               higher octave, etc.)
	 * @return the pitch (midi note number)
	 */
	public int get(int index, int octave) {
		return pitches.get(index) + (octave*12);
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return pitches.iterator();
	}
	
	public static Scale major(int start) {
		return makeScale(start, MAJOR_INTERVALS);
	}
	
	public static Scale naturalMinor(int start) {
		return makeScale(start, NATURAL_MINOR_INTERVALS);
	}
	
	public static Scale harmonicMinor(int start) {
		return makeScale(start, HARMONIC_MINOR_INTERVALS);
	}
	
	public static Scale melodicMinor(int start) {
		return makeScale(start, MELODIC_MINOR_INTERVALS);
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
