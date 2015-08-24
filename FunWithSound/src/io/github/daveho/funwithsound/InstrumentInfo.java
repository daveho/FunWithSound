package io.github.daveho.funwithsound;

import io.github.daveho.gervill4beads.GervillUGen;

import java.util.ArrayList;
import java.util.List;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;

/**
 * The GervillUGen and effects chain Beads for
 * a specific {@link Instrument}.
 */
class InstrumentInfo {
	/** GervillUGen, which sits in front of the effects chain. */
	GervillUGen gervill;
	
	/** Reference to the UGen currently at the end of the effects chain. */ 
	UGen endOfChain;

	/** Gain UGen (which is fed by the UGen at the end of the effects chain.) */
	Gain gain;
	
	List<GainEvent> gainEvents;
	
	public InstrumentInfo(GervillUGen gervill) {
		this.gervill = gervill;
		this.gainEvents = new ArrayList<GainEvent>();
		
		// Initially, the GervillUGen is at the end of the effects
		// chain (since there are no effects yet.)
		this.endOfChain = gervill;
	}
}