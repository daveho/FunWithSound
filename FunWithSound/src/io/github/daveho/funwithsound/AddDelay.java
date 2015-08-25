package io.github.daveho.funwithsound;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.CombFilter;

/**
 * Add a delay effect.
 */
public class AddDelay implements AddEffect {
	private double delayMs;
	private double passThruGain;
	private double delayGain;

	/**
	 * Constructor.
	 * 
	 * @param delayMs       the delay in milliseconds
	 * @param passThruGain  the gain for the original signal
	 * @param delayGain     the gain for the delayed signal
	 */
	public AddDelay(double delayMs, double passThruGain, double delayGain) {
		this.delayMs = delayMs;
		this.passThruGain = passThruGain;
		this.delayGain = delayGain;
	}

	@Override
	public UGen apply(AudioContext ac, InstrumentInfo info) {
		int delaySamples = (int) Math.ceil(ac.msToSamples(delayMs));
		
		CombFilter comb = new CombFilter(ac, delaySamples);
		
		comb.setDelay(delaySamples);
		comb.setA((float)passThruGain);
		comb.setG((float)delayGain);
		
		comb.addInput(info.endOfChain);
		
		return comb;
	}

}
