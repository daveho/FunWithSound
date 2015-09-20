package io.github.daveho.funwithsound;

import net.beadsproject.beads.core.UGen;

public interface Voice extends PlayNote {
	public UGen getOutput();
	
	// TODO: could add methods for other kinds of MIDI messages
}
