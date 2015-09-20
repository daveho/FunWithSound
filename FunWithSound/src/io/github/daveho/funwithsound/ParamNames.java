package io.github.daveho.funwithsound;

public interface ParamNames {
	/** DataBead property name: Glide time between notes (for portamento). */
	public static final String GLIDE_TIME_MS = "glideTimeMs";
	/** DataBead property name: Time to ramp up to full gain when note starts. */
	public static final String ATTACK_TIME_MS = "attackTimeMs";
	/** DataBead property name: Time to decay to silence when note ends. */
	public static final String RELEASE_TIME_MS = "releaseTimeMs";
	/** DataBead property name: Minimum gain (for notes with velocity 0.) */
	public static final String MIN_GAIN = "minGain";

}
