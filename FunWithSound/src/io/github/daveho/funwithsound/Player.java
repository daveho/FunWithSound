package io.github.daveho.funwithsound;

import io.github.daveho.gervill4beads.GervillUGen;
import io.github.daveho.gervill4beads.Midi;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import com.sun.media.sound.SF2Soundbank;
import com.sun.media.sound.SoftSynthesizer;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;

/**
 * Play a composition.
 */
public class Player {
	// Delay before starting playback,
	// to avoid early audio buffer underruns.
	private static final long START_DELAY_US = 1000000L;
	
	// Shut down this many microseconds after the last note off message.
	private static final long IDLE_WAIT_US = 1000000L;
	
	private Composition composition;
	private AudioContext ac;
	private HashMap<String, SF2Soundbank> soundBanks;
	
	public Player() {
		soundBanks = new HashMap<String, SF2Soundbank>();
	}
	
	public void setComposition(Composition composition) {
		this.composition = composition;
	}
	
	protected GervillUGen createGervill() throws MidiUnavailableException {
		GervillUGen result = new GervillUGen(ac, Collections.<String, Object>emptyMap());
		ac.out.addInput(result);
		return result;
	}

	public void play() throws MidiUnavailableException, IOException {
		// Create an AudioContext
		this.ac = new AudioContext();
		
		// We will create as many GervillUGens as Instruments
		Map<Instrument, GervillUGen> instrMap = new IdentityHashMap<Instrument, GervillUGen>();
		
		// Convert figures to MidiEvents and schedule them to be played
		long lastNoteOffUs = 0L;
		for (PlayFigureEvent e : composition) {
			Figure f = e.getFigure();
			Instrument instrument = f.getInstrument();
			GervillUGen gervill = instrMap.get(instrument);
			if (gervill == null) {
				gervill = createGervill();
				if (instrument.getType() == InstrumentType.MIDI_SOUNDFONT) {
					SoftSynthesizer synth = gervill.getSynth();
					SF2Soundbank sb = getSoundBank(instrument);
					synth.loadAllInstruments(sb);
				}
				if (instrument.getPatch() >= 0) {
					ShortMessage programChange = Midi.createShortMessage(ShortMessage.PROGRAM_CHANGE, instrument.getPatch());
					gervill.getSynthRecv().send(programChange, -1L);
				}
				instrMap.put(instrument, gervill);
			}
			Rhythm rhythm = f.getRhythm();
			Melody melody = f.getMelody();
			int n = Math.min(rhythm.size(), melody.size());
			for (int i = 0; i < n; i++) {
				Strike s = rhythm.get(i);
				Chord c = melody.get(i);
				for (Integer note : c) {
					// Percussion events play on channel 10, normal MIDI
					// events play on channel 1.  (Note that 1 is encoded as
					// 0, and 10 is encoded as 9.)
					int channel = instrument.getType() == InstrumentType.MIDI_PERCUSSION ? 9 : 0;
					
					long onTime = START_DELAY_US + e.getStartUs() + s.getStartUs();
					long offTime = onTime + s.getDurationUs();
					ShortMessage noteOn = Midi.createShortMessage(ShortMessage.NOTE_ON|channel, note, s.getVelocity());
					gervill.getSynthRecv().send(noteOn, onTime);
					ShortMessage noteOff = Midi.createShortMessage(ShortMessage.NOTE_OFF|channel, note, s.getVelocity());
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

	private SF2Soundbank getSoundBank(Instrument instrument) throws IOException {
		SF2Soundbank sb = null;
		if (!soundBanks.containsKey(instrument.getSoundFont())) {
			File file = new File(instrument.getSoundFont());
			if (file.exists()) {
				sb = new SF2Soundbank(file);
			}
			soundBanks.put(instrument.getSoundFont(), sb);
		}
		sb = soundBanks.get(instrument.getSoundFont());
		return sb;
	}
}
