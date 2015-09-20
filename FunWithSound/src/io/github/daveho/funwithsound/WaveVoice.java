package io.github.daveho.funwithsound;

import javax.sound.midi.ShortMessage;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.WavePlayer;

public class WaveVoice implements Voice {
	private AudioContext ac;
	private UGen freqController;
	private Buffer waveform;
	private WavePlayer player;
	
	public WaveVoice(AudioContext ac, UGen freqController, Buffer waveform) {
		this.ac = ac;
		this.freqController = freqController;
		this.waveform = waveform;
	}
	
	@Override
	public UGen getOutput() {
		if (player == null) {
			System.out.printf("Creating WavePlayer for WaveVoice\n");
			player = new WavePlayer(ac, freqController, waveform);
		}
		return player;
	}

	@Override
	public void noteOn(ShortMessage smsg, int note) {
		// Nothing to do
		System.out.printf("Note %d on!\n", note);
	}

	@Override
	public void noteOff(ShortMessage smsg, int note) {
		// Nothing to do
		System.out.printf("Note %d off!\n", note);
	}

}
