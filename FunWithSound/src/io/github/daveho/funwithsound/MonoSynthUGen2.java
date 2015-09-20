package io.github.daveho.funwithsound;

import io.github.daveho.gervill4beads.Midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGenChain;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Glide;

public class MonoSynthUGen2 extends UGenChain {
	private static class RealizedVoice {
		public RealizedVoice(Voice v, VoiceEnvelope e) {
			this.voice = v;
			this.envelope = e;
		}
		Voice voice;
		VoiceEnvelope envelope;
	}
	
	private RealizedVoice[] voices;
	private Glide freqController;
	private int note;

	public MonoSynthUGen2(
			AudioContext ac,
			Buffer buffer,
			DataBead params,
			double[] freqMult,
			double[] oscGains) {
		super(ac, 0, 2);
		
		voices = new RealizedVoice[freqMult.length];
		
		freqController = new Glide(ac);
		float glideTimeMs = Util.getFloat(params, ParamNames.GLIDE_TIME_MS);
		System.out.printf("Glide time=%f ms\n", glideTimeMs);
		freqController.setGlideTime(glideTimeMs);
		
		for (int i = 0; i < freqMult.length; i++) {
			//UGen freqMultiple = Util.multiply(freqController, freqMult[i]);
			// TODO: make voice and envelope creation configurable
			//Voice v = new WaveVoice(ac, freqMultiple, buffer);
			Voice v = new WaveVoice(ac, freqController, buffer);
			VoiceEnvelope e = new ASRVoiceEnvelope(ac, params, v.getOutput());
			RealizedVoice rv = new RealizedVoice(v, e);
			voices[i] = rv;
			addToChainOutput(rv.envelope.getOutput());
		}
	}
	
	@Override
	protected void messageReceived(Bead message) {
		if (Midi.hasMidiMessage(message)) {
			MidiMessage msg = Midi.getMidiMessage(message);
			if (msg instanceof ShortMessage) {
				ShortMessage smsg = (ShortMessage) msg;
				int note = smsg.getData1();
				if (smsg.getCommand() == ShortMessage.NOTE_ON) {
					onNoteOn(smsg, note);
				} else if (smsg.getCommand() == ShortMessage.NOTE_OFF) {
					onNoteOff(smsg, note);
				}
			}
		}
	}

	private void onNoteOn(ShortMessage smsg, int note) {
		float prevFreq = freqController.getValue(0, 0);
		System.out.printf("Previous frequency: %f\n", prevFreq);
		this.note = note;
		float freq = Pitch.mtof(note);
		System.out.printf("Note=%d, Set note frequency to %f\n", note, freq);
		freqController.setValue(freq); // glide to the note
		for (RealizedVoice rv : voices) {
			rv.voice.noteOn(smsg, note);
			rv.envelope.noteOn(smsg, note);
		}
	}

	private void onNoteOff(ShortMessage smsg, int note) {
		if (this.note == note) {
			for (RealizedVoice rv : voices) {
				rv.voice.noteOff(smsg, note);
				rv.envelope.noteOff(smsg, note);
			}
		}
	}

}
