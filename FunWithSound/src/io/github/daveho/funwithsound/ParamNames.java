package io.github.daveho.funwithsound;

/**
 * Parameter names for components that take configuration from
 * DataBeads.
 */
public interface ParamNames {
	/** DataBead property name: Glide time between notes (for portamento). */
	public static final String GLIDE_TIME_MS = "glideTimeMs";
	/** DataBead property name: Time to ramp up to full gain when note starts. */
	public static final String ATTACK_TIME_MS = "attackTimeMs";
	/** DataBead property name: Time to decay to silence when note ends. */
	public static final String RELEASE_TIME_MS = "releaseTimeMs";
	/** DataBead property name: Minimum gain (for notes with velocity 0.) */
	public static final String MIN_GAIN = "minGain";
	
	// Used by RingModulationVoice
	/** The multiple of the note frequency that should be used to generate the modulator frequency. */
	public static final String MOD_FREQ_MULTIPLE = "modFreqMultiple";
	/** Glide time for changes in the modulation frequency. */
	public static final String MOD_GLIDE_TIME_MS = "modGlideTimeMs";

}