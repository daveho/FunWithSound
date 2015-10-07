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

package io.github.daveho.funwithsound;

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
	
	Scale() {
		pitches = new ArrayList<Integer>();
	}
	
	/**
	 * Add a pitch to the scale.
	 * 
	 * @param pitch the pitch to add
	 */
	public void add(int pitch) {
		pitches.add(pitch);
	}
	
	/**
	 * Return the number of pitches in the scale.
	 * 
	 * @return the number of pitches
	 */
	public int size() {
		return pitches.size();
	}
	
	/**
	 * Get the midi note number of the specified note
	 * in the scale.  If the parameter is outside the range 0..<i>n</i>-1,
	 * where <i>n</i> is the number of notes in the scale, then
	 * the note is in an octave above or below the default
	 * octave for the scale.  E.g., for a hepatonic scale,
	 * 7 is one octave above note 0, -7 is one octave below
	 * note 0, etc.
	 * 
	 * @param index the note in the scale
	 * @return the midi note number
	 */
	public int get(int index) {
		int octaveOffset = index / pitches.size();
		int noteOffset = index % pitches.size();
		if (noteOffset < 0) {
			octaveOffset--;
			noteOffset = pitches.size() + noteOffset;
		}
		return pitches.get(noteOffset) + octaveOffset*12;
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return pitches.iterator();
	}
	
	/**
	 * Create a major scale rooted at the given MIDI note number.
	 * 
	 * @param start the root MIDI note number
	 * @return the major {@link Scale}
	 */
	public static Scale major(int start) {
		return makeScale(start, MAJOR_INTERVALS);
	}
	
	/**
	 * Create a natural minor scale rooted at the given MIDI note number.
	 * 
	 * @param start the root MIDI note number
	 * @return the natural minor {@link Scale}
	 */
	public static Scale naturalMinor(int start) {
		return makeScale(start, NATURAL_MINOR_INTERVALS);
	}
	
	/**
	 * Create a harmonic minor scale rooted at the given MIDI note number.
	 * 
	 * @param start the root MIDI note number
	 * @return the harmonic minor {@link Scale}
	 */
	public static Scale harmonicMinor(int start) {
		return makeScale(start, HARMONIC_MINOR_INTERVALS);
	}
	
	/**
	 * Create a melodic minor scale rooted at the given MIDI note number.
	 * 
	 * @param start the root MIDI note number
	 * @return the melodic minor {@link Scale}
	 */
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
