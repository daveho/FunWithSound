package edu.ycp.cs.funwithsound;

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
			cmp = Util.compareInts(o1.getStartUs(), o2.getStartUs());
			if (cmp != 0) {
				return cmp;
			}
			
			// Compare by duration and velocity as a tie-breaker
			cmp = Util.compareInts(o1.getDurationUs(), o2.getDurationUs());
			if (cmp != 0) {
				return cmp;
			}
			return Util.compareInts(o1.getVelocity(), o2.getVelocity());
		}
	};

	private int startUs;
	private int durationUs;
	private int velocity;
	
	public Strike() {
		
	}
	
	public Strike(int startUs, int durationUs, int velocity) {
		this.startUs = startUs;
		this.durationUs = durationUs;
		this.velocity = velocity;
	}
	
	public void setStartUs(int startUs) {
		this.startUs = startUs;
	}
	
	public int getStartUs() {
		return startUs;
	}
	
	public void setDurationUs(int durationUs) {
		this.durationUs = durationUs;
	}
	
	public int getDurationUs() {
		return durationUs;
	}
	
	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}
	
	public int getVelocity() {
		return velocity;
	}
}
