package io.github.daveho.funwithsound;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;

/**
 * Interface for objects that add an effect to an
 * {@link InstrumentInfo}'s effects chain.
 */
public interface AddEffect {
	/**
	 * Update the given {@link InstrumentInfo} to add an effect.
	 * 
	 * @param ac   the AudioContext
	 * @param info the {@link InstrumentInfo}
	 * @return the UGen that is the output of the effect: this will become
	 *         the new end of the effects chain
	 */
	public UGen apply(AudioContext ac, InstrumentInfo info);
}
