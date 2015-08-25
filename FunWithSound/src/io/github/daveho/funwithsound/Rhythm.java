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

import java.util.Iterator;
import java.util.TreeSet;

/**
 * A rhythm is a sequence of {@link Strike}s.
 * It specifies a rhythm pattern, but does not specify
 * notes or instruments.
 */
public class Rhythm implements Iterable<Strike> {
	private TreeSet<Strike> strikes;
	
	public Rhythm() {
		strikes = new TreeSet<Strike>(Strike.COMPARE_BY_START);
	}
	
	public void add(Strike strike) {
		strikes.add(strike);
	}
	
	public int size() {
		return strikes.size();
	}
	
	public Strike get(int index) {
		Iterator<Strike> i = strikes.iterator();
		int count = 0;
		while (i.hasNext()) {
			Strike s = i.next();
			if (count == index) {
				return s;
			}
			count++;
		}
		throw new IndexOutOfBoundsException();
	}
	
	@Override
	public Iterator<Strike> iterator() {
		return strikes.iterator();
	}
}
