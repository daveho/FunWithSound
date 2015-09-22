package io.github.daveho.funwithsound;

import javax.sound.midi.ShortMessage;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * A {@link Voice} implementation that uses frequency modulation.
 * The modulator oscillates at a multiple of the note base frequency,
 * and its output controls the frequency of the carrier.
 * The range of the modulator (minimum and maximum frequencies
 * sent to the carrier) it also based on multiples of the note
 * frequency.  I'm not sure whether this is really a
 * correct implementation of FM synthesis, but it sounds cool.
 */
public class FMVoice implements ParamNames, Voice {
	private DataBead params;
	private Glide modFreq;
	private WavePlayer player;

	/**
	 * Constructor.
	 * 
	 * @param ac the AudioContext
	 * @param params parameters
	 * @param modWaveform waveform of modulator signal
	 * @param carrierWaveForm waveform of carrier signal
	 * @param freq frequency controller UGen
	 */
	public FMVoice(AudioContext ac, DataBead params, Buffer modWaveform, Buffer carrierWaveForm, UGen freq) {
		this.params = params;
		this.modFreq = new Glide(ac);
		modFreq.setGlideTime(Util.getFloat(params, MOD_GLIDE_TIME_MS));
//		UGen mod = Util.rangedSineFunction(ac, -1, 1, modFreq);
//		UGen mod = new WavePlayer(ac, modFreq, modWaveform);
//		UGen mod = Util.rangedOscillator(ac, -1, 1, modFreq, Buffer.SAW);
		float minFreqMultiple = Util.getFloat(params, MIN_FREQ_MULTIPLE);
		float maxFreqMultiple = Util.getFloat(params, MAX_FREQ_MULTIPLE);
		UGen mod = Util.rangedOscillator(ac, minFreqMultiple, maxFreqMultiple, modFreq, modWaveform);
		
		UGen modulatedFrequency = new Function(freq, mod) {
			@Override
			public float calculate() {
				return x[0] * x[1];
			}
		};
		
		this.player = new WavePlayer(ac, modulatedFrequency, carrierWaveForm);
	}

	@Override
	public UGen getOutput() {
		return player;
	}

	@Override
	public void noteOn(ShortMessage smsg, int note) {
		float modFreqMult = Util.getFloat(params, MOD_FREQ_MULTIPLE, .5);
		float nextModFreq = modFreqMult*Pitch.mtof(note);
		//System.out.printf("Setting mod freq=%f\n", nextModFreq);
		modFreq.setValue(nextModFreq);
	}

	@Override
	public void noteOff(ShortMessage smsg, int note) {
	}
}
