package io.github.daveho.funwithsound;

import net.beadsproject.beads.data.DataBead;

/**
 * Create reasonable defaults for various components, effects, etc.
 */
public class Defaults implements ParamNames {
	/**
	 * Get some reasonable monosynth default params (e.g., for {@link MonoSynthUGen2}). 
	 * @return reasonable monosynth defaults
	 */
	public static DataBead monosynthDefaults() {
		DataBead params = new DataBead();
		params.put(GLIDE_TIME_MS, 200f);
		params.put(ATTACK_TIME_MS, 20f);
		params.put(RELEASE_TIME_MS, 200f);
		params.put(MIN_GAIN, 0.1f);
		return params;
	}

	/**
	 * Get some reasonable defaults for {@link RingModulationVoice}.
	 * @return reasonable defaults for {@link RingModulationVoice}
	 */
	public static DataBead ringModulationVoiceDefaults() {
		DataBead params = new DataBead();
		params.put(MOD_FREQ_MULTIPLE, 2);
		params.put(MOD_GLIDE_TIME_MS, 40);
		return params;
	}
	
	/**
	 * Get some reasonable defaults for {@link FMVoice}.
	 * @return reasonable defaults for {@link FMVoice}
	 */
	public static DataBead fmVoiceDefaults() {
		DataBead params = ringModulationVoiceDefaults();
		// Oddly, WavePlayer seems to work even if you give it
		// negative frequencies.
		params.put(MIN_FREQ_MULTIPLE, -1.0f);
		params.put(MAX_FREQ_MULTIPLE, 1.0f);
		return params;
	}

	/**
	 * Get some reasonable defaults for {@link BandpassFilterNoteEnvelopeAdapter}.
	 * @return reasonable {@link BandpassFilterNoteEnvelopeAdapter} defaults
	 */
	public static DataBead bandpassNoteEnvelopeDefaults() {
		DataBead params = new DataBead();
		params.put(START_END_FREQ_FACTOR, .5f);
		params.put(RISE_FREQ_FACTOR, 2f);
		params.put(RISE_TIME_MS, 200f);
		params.put(FALL_TIME_MS, 1000f);
		params.put(CURVATURE, .25);
		return params;
	}

	/**
	 * Create default flanger parameters.
	 * They are completely arbitrary, but
	 * can serve as a useful starting point.
	 * 
	 * @return default flanger parameters
	 */
	public static final DataBead flangerDefaults() {
		return new DataBead(
				FREQ_HZ, 1.0,
				MIN_DELAY_MS, 5.0,
				MAX_DELAY_MS, 10.0,
				A, .8,
				G, .8,
				H, .3
		);
	}

	/**
	 * Get default reverb parameters.
	 * 
	 * @return default reverb parameters
	 */
	public static DataBead reverbDefaults() {
		DataBead params = new DataBead();
		
		// These are the defaults for the Reverb UGen
		params.put(DAMPING, .7f);
		params.put(ROOM_SIZE, .5f);
		params.put(EARLY_REFLECTIONS_LEVEL, 1.0f);
		params.put(LATE_REVERB_LEVEL, 1.0f);
		
		return params;
	}
}
