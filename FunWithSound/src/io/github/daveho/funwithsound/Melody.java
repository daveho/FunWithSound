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
