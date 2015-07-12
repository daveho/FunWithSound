package edu.ycp.cs.funwithsound;

import io.github.daveho.gervill4beads.GervillUGen;
import io.github.daveho.gervill4beads.Midi;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;

/**
 * Play a composition.
 */
public class Player {
	// Delay before starting playback,
	// to avoid early audio buffer underruns.
	private static final long START_DELAY_US = 500000L;
	
	// Shut down this many microseconds after the last note off message.
	private static final long IDLE_WAIT_US = 1000000L;
	
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
		long lastNoteOffUs = 0L;
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
					long onTime = START_DELAY_US + f.getStartUs() + s.getStartUs();
					long offTime = onTime + s.getDurationUs();
					ShortMessage noteOn = Midi.createShortMessage(ShortMessage.NOTE_ON, note, s.getVelocity());
					gervill.getSynthRecv().send(noteOn, onTime);
					ShortMessage noteOff = Midi.createShortMessage(ShortMessage.NOTE_OFF, note, s.getVelocity());
					gervill.getSynthRecv().send(noteOff, offTime);
					// Keep track of the time of the last note off event
					if (offTime > lastNoteOffUs) {
						lastNoteOffUs = offTime;
					}
				}
			}
		}
		
		final long idleTimeUs = lastNoteOffUs + IDLE_WAIT_US;

		// Register a shutdown hook to detect when playback is finished
		final CountDownLatch latch = new CountDownLatch(1); 
		ac.invokeAfterEveryFrame(new Bead() {
			@Override
			protected void messageReceived(Bead message) {
				long timestampUs = ((long)ac.getTime()) * 1000L;
				if (timestampUs >= idleTimeUs) {
					// Notify main thread that playback is complete
					latch.countDown();
				}
			}
		});
		
		// Start the AudioContext!
		ac.start();
		
		// Wait for playback to complete, then stop the AudioContext
		try {
			latch.await();
		} catch (InterruptedException e) {
			System.out.println("Interrupted waiting for playback to complete");
		}
		ac.stop();
		System.out.println("Playback finished");
	}
}
