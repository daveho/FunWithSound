package io.github.daveho.funwithsound;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.WavePlayer;

public class WaveVoice implements Voice {

	private WavePlayer output;

	public WaveVoice(AudioContext ac, Buffer buffer, UGen multFreq) {
		this.output = new WavePlayer(ac, multFreq, buffer);
	}

	@Override
	public UGen getOutput() {
		return output;
	}
	
}
