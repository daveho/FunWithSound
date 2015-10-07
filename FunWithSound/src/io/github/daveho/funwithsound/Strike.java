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

import java.util.Comparator;

/**
 * A strike represents a note or chord being played
 * at a particular time, with a specified duration and
 * velocity.  A strike is the basic element of a
 * {@link Rhythm}.
 */
public class Strike {
	public static final Comparator<Strike> COMPARE_BY_START = new Comparator<Strike>() {
		public int compare(Strike o1, Strike o2) {
			int cmp;
			
			// Compare by start time first
			cmp = Util.compareLongs(o1.getStartUs(), o2.getStartUs());
			if (cmp != 0) {
				return cmp;
			}
			
			// Compare by duration and velocity as a tie-breaker
			cmp = Util.compareLongs(o1.getDurationUs(), o2.getDurationUs());
			if (cmp != 0) {
				return cmp;
			}
			return Util.compareInts(o1.getVelocity(), o2.getVelocity());
		}
	};

	private final long startUs;
	private final long durationUs;
	private final int velocity;
	
	/**
	 * Constructor.
	 * 
	 * @param startUs the start time (in microseconds)
	 * @param durationUs the duration (in microseconds)
	 * @param velocity the velocity (in the range 0..127)
	 */
	public Strike(long startUs, long durationUs, int velocity) {
		this.startUs = startUs;
		this.durationUs = durationUs;
		this.velocity = velocity;
	}
	
	/**
	 * Get the start time (in microseconds).
	 * 
	 * @return the start time (in microseconds)
	 */
	public long getStartUs() {
		return startUs;
	}
	
	/**
	 * Get the duration (in microseconds).
	 * 
	 * @return the duration (in microseconds)
	 */
	public long getDurationUs() {
		return durationUs;
	}
	
	/**
	 * Get the velocity (in the range 0..127).
	 * 
	 * @return the velocity (in the range 0..127)
	 */
	public int getVelocity() {
		return velocity;
	}
}
