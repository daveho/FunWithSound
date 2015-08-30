package io.github.daveho.funwithsound;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Panner;

public class AddStaticPan implements AddEffect {
	private double pos;
	
	public AddStaticPan(double pos) {
		this.pos = pos;
	}

	@Override
	public UGen apply(AudioContext ac, InstrumentInfo info) {
		Panner panner = new Panner(ac, (float)pos);
		
		panner.addInput(info.tail);
		
		return panner;
	}

}
