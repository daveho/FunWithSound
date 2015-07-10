package edu.ycp.cs.funwithsound;

import io.github.daveho.gervill4beads.GervillUGen;
import io.github.daveho.gervill4beads.Midi;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import net.beadsproject.beads.core.AudioContext;

/**
 * Play a composition.
 */
public class Player {
	private Composition composition;
	private AudioContext ac;
	
	public Player() {
	}
	
	public void setComposition(Composition composition) {
		this.composition = composition;
	}
	
	protected GervillUGen createGervill() throws MidiUnavailableException {
		GervillUGen result = new GervillUGen(ac, Collections.<String, Object>emptyMap());
		ac.out.addInput(result);
		return result;
	}

	public void play() throws MidiUnavailableException {
		
		// Create an AudioContext
		this.ac = new AudioContext();
		
		// We will create as many GervillUGens as Instruments
		Map<Instrument, GervillUGen> instrMap = new IdentityHashMap<Instrument, GervillUGen>();
		
		// Convert figures to MidiEvents and schedule them to be played
		for (Figure f : composition) {
			Instrument instrument = f.getInstrument();
			GervillUGen gervill = instrMap.get(instrument);
			if (gervill == null) {
				gervill = createGervill();
				ShortMessage programChange = Midi.createShortMessage(ShortMessage.PROGRAM_CHANGE, instrument.getPatch());
				gervill.getSynthRecv().send(programChange, -1L);
				instrMap.put(instrument, gervill);
			}
			Rhythm rhythm = f.getRhythm();
			Melody melody = f.getMelody();
			int n = Math.min(rhythm.size(), melody.size());
			for (int i = 0; i < n; i++) {
				Strike s = rhythm.get(i);
				Chord c = melody.get(i);
				for (Integer note : c) {
					ShortMessage noteOn = Midi.createShortMessage(ShortMessage.NOTE_ON, note, s.getVelocity());
					gervill.getSynthRecv().send(noteOn, f.getStartUs() + s.getStartUs());
					ShortMessage noteOff = Midi.createShortMessage(ShortMessage.NOTE_OFF, note, s.getVelocity());
					gervill.getSynthRecv().send(noteOff, f.getStartUs() + s.getStartUs() + s.getDurationUs());
				}
			}
		}
		
		// Start the AudioContext!
		ac.start();
	}
}
