package io.github.daveho.funwithsound;

/**
 * Parameter names for components that take configuration from
 * DataBeads.
 */
public interface ParamNames {
	// Used by MonoSynthUGen2
	/** DataBead property name: Glide time between notes (for portamento). */
	public static final String GLIDE_TIME_MS = "glideTimeMs";
	
	// Used by ASRNoteEnvelope
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

	// Used by BandpassFilterNoteEnvelopeAdapter 
	/** DataBead property name: Start frequency (expressed as a multiple of the note frequency). */
	public static final String START_END_FREQ_FACTOR = "startEndFreqFactor";
	/** DataBead property name: Rise frequency (expressed as a multiple of the note frequency). */
	public static final String RISE_FREQ_FACTOR = "riseFreqFactor";
	/** DataBead property name: Time to rise from the start frequency to the rise frequency. */
	public static final String RISE_TIME_MS = "riseTimeMs";
	/** DataBead property name: Time to decay from the rise frequency back to the start frequency. */
	public static final String FALL_TIME_MS = "fallTimeMs";
	/** DataBead property name: Curvature of the glides from start to rise and back. */
	public static final String CURVATURE = "curvature";
}