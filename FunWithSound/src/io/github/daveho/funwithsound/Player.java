// Copyright 2015, David Hovemeyer <david.hovemeyer@gmail.com>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package io.github.daveho.funwithsound;

import io.github.daveho.gervill4beads.CaptureMidiMessages;
import io.github.daveho.gervill4beads.GervillUGen;
import io.github.daveho.gervill4beads.Midi;
import io.github.daveho.gervill4beads.MidiMessageAndTimeStamp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.RecordToFile;

import com.sun.media.sound.SF2Soundbank;
import com.sun.media.sound.SoftSynthesizer;

/**
 * Play a composition.
 */
public class Player {
	// Delay before starting playback,
	// to avoid early audio buffer underruns.
	private static final long START_DELAY_US = 1000000L;
	
	// Shut down this many microseconds after the last note off message.
	private static final long IDLE_WAIT_US = 2000000L;
	
	private Composition composition;
	private AudioContext ac;
	private HashMap<String, SF2Soundbank> soundBanks;
	private InstrumentInfo liveSynth;
	private Map<Instrument, InstrumentInfo> instrMap;
	private String outputFile;
	private long idleTimeUs;
	private CountDownLatch latch;
	private ArrayList<MidiMessageAndTimeStamp> capturedEvents;
	private MidiDevice device;
	private boolean playing;
	
	public Player() {
		soundBanks = new HashMap<String, SF2Soundbank>();
		instrMap = new IdentityHashMap<Instrument, InstrumentInfo>();
	}

	/**
	 * Get the current timestamp in microseconds from the
	 * AudioContext.
	 * 
	 * @return the current timestamp in microseconds
	 */
	public long getCurrentTimestamp() {
		double timeMs = ac.getTime();
		return (long) (timeMs * 1000.0);
	}
	
	/**
	 * Check whether the player is playing asynchronously.
	 * 
	 * @return true if the player is playing, false if not
	 */
	public boolean isPlaying() {
		return playing;
	}
	
	public void setComposition(Composition composition) {
		this.composition = composition;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
	
	private InstrumentInfo createGervill(Instrument instrument) throws MidiUnavailableException, IOException {
		// Note that the GervillUGen isn't connected to an effects chain,
		// or the AudioContext output, at this point.
		GervillUGen gervill = new GervillUGen(ac, Collections.<String, Object>emptyMap());
		InstrumentInfo info = new InstrumentInfo(gervill);
		if (instrument.hasSoundFont()) {
			SoftSynthesizer synth = ((GervillUGen)info.head).getSynth();
			SF2Soundbank sb = getSoundBank(instrument);
			if (sb != null) {
				synth.loadAllInstruments(sb);
			} else {
				System.err.println("Warning: couldn't load soundfont " + instrument.getSoundFont());
			}
		}
		int patch = instrument.getPatch();
		if (patch >= 1) {
			// The MIDI patches are numbered 1..128, but encoded as 0..127
			patch--;
			ShortMessage programChange = Midi.createShortMessage(ShortMessage.PROGRAM_CHANGE, patch);
			((GervillUGen)info.head).getSynthRecv().send(programChange, -1L);
		}
		return info;
	}

	/**
	 * Play the composition synchronously (if playing live),
	 * or render it to an output file (if {@link #setOutputFile(String)}
	 * was called.)
	 * 
	 * @throws MidiUnavailableException
	 * @throws IOException
	 */
	public void play() throws MidiUnavailableException, IOException {
		prepareToPlay();
		
		if (outputFile != null) {
			renderToOutputFile();
		} else {
			playLiveAndWait();
		}
		
		onPlayingFinished();
	}
	
	/**
	 * Start playing the composition asynchronously.
	 */
	public void startPlaying() {
		try {
			prepareToPlay();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ac.start();
		this.playing = true;
	}
	
	public void checkForEndOfPlaying() {
		if (playing && latch.getCount() == 0) {
			onPlayingFinished();
			playing = false;
		}
	}

	public void forceStopPlaying() {
		if (playing) {
			if (latch.getCount() > 0) {
				ac.stop();
			}
			onPlayingFinished();
			playing = false;
		}
	}

	private void onPlayingFinished() {
		// If we opened a MIDI device, close it
		if (device != null) {
			device.close();
		}
		
		// If MIDI messages were captured, translate them to
		// Rhythm and Melody
		if (capturedEvents != null && !capturedEvents.isEmpty()) {
			analyzeCapturedEvents(capturedEvents);
		}
	}

	private void playLiveAndWait() {
		// Start the AudioContext! (for real-time output)
		ac.start();
		// Wait for playback to complete, then stop the AudioContext
		try {
			latch.await();
		} catch (InterruptedException e) {
			System.out.println("Interrupted waiting for playback to complete");
		}
		System.out.println("Playback finished");
	}

	private void renderToOutputFile() throws IOException {
		System.out.print("Saving audio data to " + outputFile + "...");
		System.out.flush();
		File f = new File(outputFile);
		RecordToFile recorder = new RecordToFile(ac, 2, f);
		recorder.addInput(ac.out);
		ac.out.addDependent(recorder);
		// Render to file
		ac.logTime(true);
		ac.runForNMillisecondsNonRealTime(idleTimeUs / 1000L);
		System.out.println("done!");
	}

	/**
	 * Prepare the Beads AudioContext to play the composition and
	 * (if there is one) the live audition part.
	 * Subclasses may override.
	 * 
	 * @throws MidiUnavailableException
	 * @throws IOException
	 */
	protected void prepareToPlay() throws MidiUnavailableException, IOException {
		// Create an AudioContext
		this.ac = new AudioContext();
		
		this.idleTimeUs = prepareComposition();
		System.out.printf("Idle time at %d us\n", this.idleTimeUs);

		// Register a shutdown hook to detect when playback is finished
		this.latch = new CountDownLatch(1); 
		addShutdownHook(idleTimeUs);
		
		// If there is a live instrument, create a synthesizer for it,
		// and arrange to feed live midi events to it
		prepareForAudition();

		// Add gain events
		addGainEvents();
		
		// Prepare instrument effects (and connect the GervillUGens
		// to the AudioContext's output)
		configureInstrumentEffects();
	}

	private void prepareForAudition() throws MidiUnavailableException,
			IOException {
		// Check the composition to see if there is an audition part
		Instrument liveInstr = composition.getAudition();
		if (liveInstr == null) {
			return;
		}
		
		this.device = null;
		this.capturedEvents = new ArrayList<MidiMessageAndTimeStamp>();
		
		// Create a message source to feed MIDI events to the Gervill instance
		createMessageSource(liveInstr);
		
		// Find a MIDI transmitter and feed its generated MIDI events to
		// the message source
		try {
			device = CaptureMidiMessages.getMidiInput(liveSynth.source);
		} catch (MidiUnavailableException e) {
			System.out.println("Warning: no MIDI input device found for live audition");
		}
	}

	private void createMessageSource(final Instrument liveInstr) throws MidiUnavailableException,
			IOException {
		this.liveSynth = getInstrumentInfo(liveInstr);

		// Filter incoming MidiMessages to:
		// - change to channel 10 (if this is a percussion instrument)
		// - add them to capturedEvents list
		final Receiver delegate = liveSynth.source;
		liveSynth.source = new Receiver() {
			@Override
			public void send(MidiMessage message, long timeStamp) {
				if (liveInstr.getType() == InstrumentType.MIDI_PERCUSSION) {
					// Percussion messages should on channel 10
					if (message instanceof ShortMessage) {
						ShortMessage smsg = (ShortMessage) message;
						message = Midi.createShortMessage(smsg.getStatus()|9, smsg.getData1(), smsg.getData2());
					}
				}

				capturedEvents.add(new MidiMessageAndTimeStamp(message, timeStamp));
				
				delegate.send(message, timeStamp);
			}
			
			@Override
			public void close() {
				delegate.close();
			}
		};
	}
	
	/**
	 * Get the Receiver that will deliver MIDI messages to
	 * the Gervill instance being used to play the live audition part.
	 * 
	 * @return the Receiver, or null if there is no live audition part
	 */
	public Receiver getReceiver() {
		return liveSynth != null ? liveSynth.source : null;
	}

	private void addGainEvents() throws MidiUnavailableException, IOException {
		// Distribute GainEvents by instrument
		for (GainEvent e : composition.getGainEvents()) {
			InstrumentInfo info = getInstrumentInfo(e.instr);
			info.gainEvents.add(e);
		}
		
		// Sort the GainEvents by timestamp for each instrument
		for (Map.Entry<Instrument, InstrumentInfo> entry : instrMap.entrySet()) {
			Collections.sort(entry.getValue().gainEvents, new Comparator<GainEvent>() {
				@Override
				public int compare(GainEvent o1, GainEvent o2) {
					if (o1.ts < o2.ts) {
						return -1;
					} else if (o1.ts > o2.ts) {
						return 1;
					} else {
						return 0;
					}
				}
			}); 
		}
	}
	
	private void configureInstrumentEffects() {
		for (Map.Entry<Instrument, InstrumentInfo> entry : instrMap.entrySet()) {
			InstrumentInfo info = entry.getValue();
			
			List<AddEffect> fx = composition.getEffectsMap().get(entry.getKey());
			if (fx != null) {
				for (AddEffect effect : fx) {
					info.tail = effect.apply(ac, info);
				}
			}
			
			UGen gainEnvelope = new InstrumentGainEnvelope(ac, info.gainEvents);
			info.gain = new Gain(ac, 2, gainEnvelope);
			info.gain.addInput(info.tail);
			ac.out.addInput(info.gain);
		}
	}

	private void addShutdownHook(final long idleTimeUs) {
		ac.invokeAfterEveryFrame(new Bead() {
			@Override
			protected void messageReceived(Bead message) {
				long timestampUs = ((long)ac.getTime()) * 1000L;
				if (timestampUs >= idleTimeUs) {
					// Notify main thread that playback is complete
					latch.countDown();
					
					System.out.println("Ready to shut down?");
					
					// I assume it's OK for a Bead to stop the AudioContext?
					ac.stop();
				}
			}
		});
	}

	private long prepareComposition() throws MidiUnavailableException, IOException {
		// Convert figures to MidiMessages and schedule them to be played
		long lastNoteOffUs = 0L;
		for (PlayFigureEvent e : composition) {
//			System.out.printf("PlayFigureEvent start time=%d\n", e.getStartUs());
			SimpleFigure f = e.getFigure();
			Instrument instrument = f.getInstrument();
			InstrumentInfo info = getInstrumentInfo(instrument);
			Rhythm rhythm = f.getRhythm();
			Melody melody = f.getMelody();
			int n = Math.min(rhythm.size(), melody.size());
			for (int i = 0; i < n; i++) {
				Strike s = rhythm.get(i);
//				System.out.printf("Strike start time=%d, duration=%d\n", s.getStartUs(), s.getDurationUs());
				Chord c = melody.get(i);
				for (Integer note : c) {
					// Percussion events play on channel 10, normal MIDI
					// events play on channel 1.  (Note that 1 is encoded as
					// 0, and 10 is encoded as 9.)
					int channel = instrument.getType() == InstrumentType.MIDI_PERCUSSION ? 9 : 0;
					
					long onTime = START_DELAY_US + e.getStartUs() + s.getStartUs();
//					System.out.printf("Note on at %d\n", onTime);
					long offTime = onTime + s.getDurationUs();
					ShortMessage noteOn = Midi.createShortMessage(ShortMessage.NOTE_ON|channel, note, s.getVelocity());
					info.source.send(noteOn, onTime);
					ShortMessage noteOff = Midi.createShortMessage(ShortMessage.NOTE_OFF|channel, note, s.getVelocity());
					info.source.send(noteOff, offTime);
					// Keep track of the time of the last note off event
					if (offTime > lastNoteOffUs) {
						lastNoteOffUs = offTime;
					}
				}
			}
		}
		
		final long idleTimeUs = lastNoteOffUs + IDLE_WAIT_US;
		return idleTimeUs;
	}

	private InstrumentInfo getInstrumentInfo(Instrument instrument)
			throws MidiUnavailableException, IOException {
		InstrumentInfo info = instrMap.get(instrument);
		if (info == null) {
			if (instrument.isMidi()) {
				info = createGervill(instrument);
			} else if (instrument.getType() == InstrumentType.SAMPLE_BANK) {
				info = createSampleBank(instrument);
			} else {
				throw new RuntimeException("Don't know how to create a " + instrument.getType() + " instrument");
			}
			instrMap.put(instrument, info);
		}
		return info;
	}
	
	private InstrumentInfo createSampleBank(Instrument instr) {
		InstrumentInfo info = instrMap.get(instr);
		if (info == null) {
			SampleBankUGen sb = new SampleBankUGen(ac);
			for (Map.Entry<Integer, SampleInfo> entry : instr.getSampleMap().entrySet()) {
				SampleInfo sampleInfo = entry.getValue();

				Sample sample = SampleManager.sample(sampleInfo.fileName);
				
				if (sampleInfo.startMs >= 0.0) {
					// Range is specified
					SampleRange sr = new SampleRange(sampleInfo.startMs, sampleInfo.endMs);
					sb.addSample(sampleInfo.note, sample, sampleInfo.gain, sr);
				} else {
					// Play entire sample
					sb.addSample(sampleInfo.note, sample, sampleInfo.gain);
				}
				
				/*
				SampleRange sampleRange = instr.getSampleRanges().get(entry.getKey());
				if (sampleRange != null) {
					sb.addSample(entry.getKey(), sample, sampleRange);
				} else {
					sb.addSample(entry.getKey(), sample);
				}
				*/
			}
			info = new InstrumentInfo(sb, ac);
			instrMap.put(instr, info);
		}
		return info;
	}

	private SF2Soundbank getSoundBank(Instrument instrument) throws IOException {
		System.out.println("Loading soundfont " + instrument.getSoundFont());
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
	
	static class NoteStart {
		final long ts;
		final int velocity;
		NoteStart(long ts, int velocity) {
			this.ts = ts;
			this.velocity = velocity;
		}
	}

	private void analyzeCapturedEvents(List<MidiMessageAndTimeStamp> capturedEvents) {
		Map<Integer, NoteStart> starts = new HashMap<Integer, NoteStart>();
		
		Rhythm rhythm = new Rhythm();
		Melody melody = new Melody();
		
		Tempo tempo = composition.getTempo();
		
		long baseTs = -1L;
		
		for (MidiMessageAndTimeStamp mmts : capturedEvents) {
			MidiMessage msg = mmts.msg;
			long ts = mmts.timeStamp;
			if (msg instanceof ShortMessage) {
				ShortMessage smsg = (ShortMessage) msg;
				if (smsg.getCommand() == ShortMessage.NOTE_ON) {
					if (baseTs < 0L && ts >= 0L) {
						baseTs = ts;
//						System.out.println("baseTs="+baseTs);
					}
					int note = smsg.getData1();
					int velocity = smsg.getData2();
					starts.put(note, new NoteStart(ts, velocity));
				} else if (smsg.getCommand() == ShortMessage.NOTE_OFF) {
					int note = smsg.getData1();
					NoteStart start = starts.get(note);
					if (start != null) {
						//System.out.printf("baseTs=%d, start.ts=%d, ts=%d\n", baseTs, start.ts, ts);
						Strike s = new Strike(start.ts - baseTs, ts - start.ts, start.velocity);
						rhythm.add(s);
						Chord ch = new Chord();
						ch.add(note);
						melody.add(ch);
					}
				}
			}
		}
		
		// FIXME: really should have a more general way of converting
		// to external form
		boolean first;
		System.out.print("Rhythm rhythm = r(\n\t");
		first = true;
		for (Strike s : rhythm) {
			double beat = s.getStartUs() / (double)tempo.getUsPerBeat();
			double duration = s.getDurationUs() / (double)tempo.getUsPerBeat();
			int velocity = s.getVelocity();
			if (first) {
				first = false;
			} else {
				System.out.print(", ");
			}
			System.out.printf("s(%.03f,%.03f,%d)", beat, duration, velocity);
		}
		System.out.println(");");

		first = true;
		System.out.print("Melody melody = m(\n\t");
		for (Chord c : melody) {
			// Each chord is guaranteed to have only one note
			int note = c.get(0);
			if (first) {
				first = false;
			} else {
				System.out.print(", ");
			}
			System.out.printf("an(%d)", note);
		}
		System.out.println(");");
	}
}
