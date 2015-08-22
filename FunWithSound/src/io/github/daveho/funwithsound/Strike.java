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
	
	public Strike(long startUs, long durationUs, int velocity) {
		this.startUs = startUs;
		this.durationUs = durationUs;
		this.velocity = velocity;
	}
	
	public long getStartUs() {
		return startUs;
	}
	
	public long getDurationUs() {
		return durationUs;
	}
	
	public int getVelocity() {
		return velocity;
	}
}
