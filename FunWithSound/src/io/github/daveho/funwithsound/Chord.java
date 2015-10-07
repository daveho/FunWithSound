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
 * A chord is a collection of one or more midi pitches (note numbers).
 * Note that most chords will actually represent a single
 * note, but it is useful to allow combinations of notes
 * to be represented (so that they are played together).
 * Note that a chord does not represent any start time or
 * duration: that is represented by a {@link Strike}.
 */
public class Chord implements Iterable<Integer>, Cloneable {
	private List<Integer> pitches;
	
	/**
	 * Constructor.
	 */
	public Chord() {
		pitches = new ArrayList<Integer>();
	}
	
	/**
	 * Add a pitch (MIDI note number).
	 * 
	 * @param pitch the pitch to add
	 */
	public void add(int pitch) {
		pitches.add(pitch);
	}
	
	/**
	 * Get the number of pitches.
	 * 
	 * @return the number of pitches
	 */
	public int size() {
		return pitches.size();
	}
	
	/**
	 * Get specified pitch.
	 * 
	 * @param index the index of the pitch
	 * @return the pitch
	 */
	public int get(int index) {
		return pitches.get(index);
	}
	
	@Override
	public Iterator<Integer> iterator() {
		return pitches.iterator();
	}
	
	@Override
	public Chord clone() {
		try {
			Chord dup = (Chord) super.clone();
			dup.pitches = new ArrayList<Integer>();
			dup.pitches.addAll(this.pitches);
			return dup;
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException("This can't happen", e);
		}
	}
}
