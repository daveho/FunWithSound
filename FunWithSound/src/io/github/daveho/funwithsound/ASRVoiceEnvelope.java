package io.github.daveho.funwithsound;

import javax.sound.midi.ShortMessage;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.Envelope;

public class ASRVoiceEnvelope implements VoiceEnvelope {
	private DataBead params;
	private Envelope envelope;

	public ASRVoiceEnvelope(AudioContext ac, DataBead params, UGen input) {
		this.params = params;
		this.envelope = new Envelope(ac);
		this.envelope.addInput(input);
		this.envelope.setValue(0.0f);
	}
	
	@Override
	public UGen getOutput() {
		return envelope;
	}

	@Override
	public void noteOn(ShortMessage smsg, int note) {
		System.out.printf("Env note on note=%d\n", note);
		envelope.clear();
		int velocity = smsg.getData2();
		float minGain = Util.getFloat(params, ParamNames.MIN_GAIN);
		float gain = minGain + ((1.0f - minGain) * (velocity/127.0f));
		float attackTimeMs = Util.getFloat(params, ParamNames.ATTACK_TIME_MS);
		System.out.printf("Ramp to gain %f in %f ms\n", gain, attackTimeMs);
		envelope.addSegment(gain, attackTimeMs);
	}

	@Override
	public void noteOff(ShortMessage smsg, int note) {
		System.out.printf("Env note off note=%d\n", note);
		envelope.addSegment(0.0f, Util.getFloat(params, ParamNames.RELEASE_TIME_MS));
	}
}
