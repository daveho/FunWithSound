package io.github.daveho.funwithsound;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Reverb;

public class AddReverb implements AddEffect {
	@Override
	public UGen apply(AudioContext ac, InstrumentInfo info) {
		Reverb reverb = new Reverb(ac, 2);
		reverb.addInput(info.endOfChain);
		onCreateReverb(ac, reverb);

		// A Gain is used to mix the reverb output with the original signal
		Gain mix = new Gain(ac, 2);
		mix.addInput(reverb);
		mix.addInput(info.endOfChain);
		
		// The Gain is now at the end of the effects chain
		return mix;
	}

	/**
	 * Subclasses may override to tweak reverb params.
	 * 
	 * @param ac      the AudioContext
	 * @param reverb  the Reverb
	 */
	public void onCreateReverb(AudioContext ac, Reverb reverb) {
		
	}
}
