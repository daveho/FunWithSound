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

	private long startUs;
	private long durationUs;
	private int velocity;
	
	public Strike() {
		
	}
	
	public Strike(long startUs, long durationUs, int velocity) {
		this.startUs = startUs;
		this.durationUs = durationUs;
		this.velocity = velocity;
	}
	
	public void setStartUs(long startUs) {
		this.startUs = startUs;
	}
	
	public long getStartUs() {
		return startUs;
	}
	
	public void setDurationUs(long durationUs) {
		this.durationUs = durationUs;
	}
	
	public long getDurationUs() {
		return durationUs;
	}
	
	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}
	
	public int getVelocity() {
		return velocity;
	}
}
