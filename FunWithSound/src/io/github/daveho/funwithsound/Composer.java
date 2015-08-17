package io.github.daveho.funwithsound;

/**
 * The composer class builds upon the model classes to provide
 * a simple domain-specific language for defining compositions
 * consisting {@link Rhythm} and {@link Melody} objects
 * rendered using {@link Instrument}.  Note that the
 * tempo and scale must be set before other methods are called.
 * Use {{@link #tempo(int, int)}} to set the tempo and one of
 * {@link #major(int)}, {@link #naturalMinor(int)}, {@link #harmonicMinor(int)},
 * or {@link #melodicMinor(int)} to set the scale.
 * The composer produces a {@link Composition}
 * object as its product.
 */
public class Composer {
	private Composition composition;
	private int measure;
	private Instrument audition;
	
	public Composer() {
		composition = new Composition();
		measure = 0;
	}
	
	protected boolean hasAudition() {
		return audition != null;
	}
	
	protected  Instrument getAudition() {
		return audition;
	}
	
	/**
	 * Create a new MIDI instrument using the built-in "emergency" MIDI
	 * soundbank.
	 * 
	 * @param patch the MIDI patch
	 * @return the instrument
	 */
	public Instrument instr(int patch) {
		return new Instrument(patch);
	}
	
	/**
	 * Create a new MIDI instrument loaded from a specified soundfont file.
	 * The instrument will play using the default patch.
	 * 
	 * @param soundFont the sound font file name
	 * @return the instrument
	 */
	public Instrument instr(String soundFont) {
		return new Instrument(soundFont);
	}
	
	/**
	 * Create a new MIDI instrument loaded from a specified soundfont file.
	 * 
	 * @param soundFont the sound font file name
	 * @param patch the patch
	 * @return the instrument
	 */
	public Instrument instr(String soundFont, int patch) {
		return new Instrument(InstrumentType.MIDI_SOUNDFONT, patch, soundFont);
	}
	
	/**
	 * Create a new MIDI percussion instrument.
	 * 
	 * @return the instrument
	 */
	public Instrument percussion() {
		return new Instrument(InstrumentType.MIDI_PERCUSSION);
	}
	
	/**
	 * Create a new MIDI percussion instrument using sounds loaded
	 * from given sound font.
	 * 
	 * @param soundFont the sound font filename
	 * @return the instrument
	 */
	public Instrument percussion(String soundFont) {
		return new Instrument(InstrumentType.MIDI_PERCUSSION, 0, soundFont);
	}
	
	/**
	 * Get the composition.
	 * 
	 * @return the composition
	 */
	public Composition getComposition() {
		return composition;
	}

	/**
	 * Set the tempo for the composition.
	 * 
	 * @param beatsPerMinute   number of beats per minute
	 * @param beatsPerMeasure  number of beats per measure
	 */
	public void tempo(int beatsPerMinute, int beatsPerMeasure) {
		composition.setTempo(new Tempo(beatsPerMinute, beatsPerMeasure));
	}
	
	/**
	 * Set the scale for the composition to a major scale,
	 * starting with the specified MIDI note number.
	 * 
	 * @param start the start note of the scale
	 */
	public void major(int start) {
		composition.setScale(Scale.major(start));
	}
	
	/**
	 * Set the scale for the composition to a natural minor scale,
	 * starting with the specified MIDI note number.
	 * 
	 * @param start the start note of the scale
	 */
	public void naturalMinor(int start) {
		composition.setScale(Scale.naturalMinor(start));
	}
	
	/**
	 * Set the scale for the composition to a harmonic minor scale,
	 * starting with the specified MIDI note number.
	 * 
	 * @param start the start note of the scale
	 */
	public void harmonicMinor(int start) {
		composition.setScale(Scale.harmonicMinor(start));
	}
	
	/**
	 * Set the scale for the composition to a melodic minor scale,
	 * starting with the specified MIDI note number.
	 * 
	 * @param start the start note of the scale
	 */
	public void melodicMinor(int start) {
		composition.setScale(Scale.melodicMinor(start));
	}

	/**
	 * Create a note or notes chosen from the composer's {@link Scale}.
	 * 
	 * @param notes the note(s), which should be in the range 0..6 for a normal
	 *              heptatonic scale, but could be above or below that range
	 *              to select notes in octaves above or below the scale's
	 *              "normal" octave
	 * @return the {@link Chord} representing the note(s)
	 */
	public Chord n(int... notes) {
		Chord chord = new Chord();
		for (int note : notes) {
			chord.add(composition.getScale().get(note));
		}
		return chord;
	}
	
	/**
	 * Create a note or notes based on absolute MIDI pitch numbers.
	 * This is useful for notes captured from a live audition.
	 * It is also useful for playing notes that aren't part of the
	 * current scaoe.
	 * 
	 * @param pitches one or more MIDI pitch numbers
	 * @return the {@link Chord} representing the notes
	 */
	public Chord an(int... pitches) {
		Chord chord = new Chord();
		for (int pitch : pitches) {
			chord.add(pitch);
		}
		return chord;
	}

	/**
	 * Create a melody consisting of the specified
	 * sequence of chords.
	 * 
	 * @param chords the chords: can be integers (to select single notes),
	 *        or {@link Chord} objects
	 * @return the melody
	 */
	public Melody m(Object... chords) {
		Melody melody = new Melody();
		for (Object o : chords) {
			if (o instanceof Number) {
				melody.add(n(((Number)o).intValue()));
			} else if (o instanceof Chord) {
				melody.add((Chord)o);
			}
		}
		return melody;
	}
	
	/**
	 * Shift the octave of a note/chord.
	 * 
	 * @param octave the octave: -1 for one octave down, 1 for one octave up, etc.
	 * @param orig   the original note/chord
	 * @return the shifted note/chord
	 */
	public Chord xn(int octave, Chord orig) {
		Chord result = new Chord();
		for (Integer note : orig) {
			result.add(note + 12*octave);
		}
		return result;
	}
	
	/**
	 * Shift the octave in a {@link Melody}. 
	 * 
	 * @param octave the octave: -1 for one octave down, 1 for one octave up, etc.
	 * @param orig the melody to shift
	 * @return the shifted melody
	 */
	public Melody xm(int octave, Melody orig) {
		Melody result = new Melody();
		for (Chord ch : orig) {
			result.add(xn(octave, ch));
		}
		return result;
	}
	
	/**
	 * Shift the octave in the melody of a {@link Figure}. 
	 * 
	 * @param octave the octave: -1 for one octave down, 1 for one octave up, etc.
	 * @param orig the figure whose melody should be shifted
	 * @return a figure with the shifted melody
	 */
	public Figure xf(int octave, Figure orig) {
		Figure result = new Figure();
		result.setRhythm(orig.getRhythm());
		result.setMelody(xm(octave, orig.getMelody()));
		result.setInstrument(orig.getInstrument());
		return result;
	}

	/**
	 * Create a full-velocity strike.
	 * 
	 * @param beat      offset in beats
	 * @param duration  duration in beats
	 * @return the strike
	 */
	public Strike s(double beat, double duration) {
		return s(beat, duration, 127);
	}
	
	/**
	 * Create a strike with a specified velocity.
	 * Higher velocity generally means a louder note/chord when played.
	 * 
	 * @param beat      offset in beats
	 * @param duration  duration in beats
	 * @param velocity  velocity, in the range 0..127
	 * @return the strike
	 */
	public Strike s(double beat, double duration, int velocity) {
		Strike strike = new Strike();
		strike.setStartUs(composition.getTempo().beatToUs(beat));
		strike.setDurationUs(composition.getTempo().beatToUs(duration));
		strike.setVelocity(velocity);
		return strike;
	}
	
	/**
	 * Create a full-velocity quarter beat strike.
	 * 
	 * @param beat offset in beats
	 * @return the strike
	 */
	public Strike qs(double beat) {
		return s(beat, .25);
	}

	/**
	 * Create a quarter beat strike with specified velocity.
	 * 
	 * @param beat      offset in beats
	 * @param velocity  velocity, in the range 0..127
	 * @return
	 */
	public Strike qs(double beat, int velocity) {
		return s(beat, .25, velocity);
	}
	
	/**
	 * Create a full-velocity half beat strike.
	 * 
	 * @param beat offset in beats
	 * @return the strike
	 */
	public Strike hs(double beat) {
		return s(beat, .5);
	}
	
	/**
	 * Create a half beat strike with specified velocity.
	 * 
	 * @param beat offset in beats
	 * @param velocity  velocity, in the range 0..127
	 * @return the strike
	 */
	public Strike hs(double beat, int velocity) {
		return s(beat, .5, velocity);
	}
	
	/**
	 * Create a full-velocity full beat strike.
	 * 
	 * @param beat offset in beats
	 * @return the strike
	 */
	public Strike fs(double beat) {
		return s(beat, 1);
	}
	
	/**
	 * Create a full beat strike with specified velocity.
	 * 
	 * @param beat offset in beats
	 * @param velocity  velocity, in the range 0..127
	 * @return the strike
	 */
	public Strike fs(double beat, int velocity) {
		return s(beat, 1, velocity);
	}
	
	/**
	 * Create a full-velocity percussive strike.
	 * 
	 * @param beat offset in beats
	 * @return the strike
	 */
	public Strike p(double beat) {
		return p(beat, 127);
	}
	
	/**
	 * Create a percussive strike with specified velocity.
	 * Higher velocity generally means a louder percussion sound when played.
	 * 
	 * @param beat offset in beats
	 * @param velocity, in the range 0..127
	 * @return the strike
	 */
	public Strike p(double beat, int velocity) {
		Strike strike = new Strike();
		strike.setStartUs(composition.getTempo().beatToUs(beat));
		strike.setDurationUs(1000000L/200L); // duration is arbitrarily 5ms
		strike.setVelocity(velocity);
		return strike;
	}
	
	/**
	 * Create a rhythm from a sequence of strikes.
	 * 
	 * @param strikes sequence of strikes
	 * @return a rhythm
	 */
	public Rhythm r(Strike... strikes) {
		Rhythm rhythm = new Rhythm();
		for (Strike s : strikes) {
			rhythm.add(s);
		}
		return rhythm;
	}
	
	/**
	 * Create a repeated rhythm containing a specified
	 * number of percussive strikes.
	 * 
	 * @param first    the first strike: the other strikes will be identical
	 *                 except for their start time
	 * @param spacing  the amount of time between the strikes,
	 *                 counted in beats
	 * @param count    how many strikes to generate
	 * @return the rhythm
	 */
	public Rhythm rr(Strike first, double spacing, int count) {
		Rhythm result = new Rhythm();
		
		for (int i = 0; i < count; i++) {
			Strike s = new Strike(
					(long) (first.getStartUs() + (i*spacing*composition.getTempo().getUsPerBeat())),
					first.getDurationUs(),
					first.getVelocity());
			result.add(s);
		}
		
		return result;
	}
	
	/**
	 * Create an instrument.
	 * 
	 * @param patch the instrument's midi patch number
	 * @return the instrument
	 */
	public Instrument i(int patch) {
		return new Instrument(patch);
	}
	
	/**
	 * Create a figure from a {@link Rhythm}, {@link Melody}, and {@link Instrument}.
	 * 
	 * @param rhythm  the rhythm
	 * @param melody  the melody
	 * @param instrument the instrument
	 * @return the figure
	 */
	public Figure f(Rhythm rhythm, Melody melody, Instrument instrument) {
		Figure result = new Figure();
		result.setRhythm(rhythm);
		result.setMelody(melody);
		result.setInstrument(instrument);
		return result;
	}
	
	/**
	 * Create a "percussion figure", in which a rhythm specifies
	 * how to play a single percussion "note", which really
	 * designates a particular MIDI percussion instrument
	 * (using the MIDI percussion key map.)  This method
	 * avoids the necessity of creating a {@link Melody}.
	 * 
	 * @param rhythm       the rhythm to play
	 * @param note         the note selecting the MIDI percussion instrument
	 * @param instrument   the instrument (which must have type
	 *                     {@link InstrumentType#MIDI_PERCUSSION}
	 * @return the percussion figure
	 */
	public Figure pf(Rhythm rhythm, int note, Instrument instrument) {
		if (instrument.getType() != InstrumentType.MIDI_PERCUSSION) {
			throw new IllegalArgumentException("pf must use a percussion instrument");
		}
		Melody m = new Melody();
		for (int i = 0; i < rhythm.size(); i++) {
			m.add(an(note));
		}
		return f(rhythm, m, instrument);
	}

	/**
	 * Add figures to play simultaneously in the current measure in
	 * the current {@link Composition}.  The start times of the
	 * {@link Strike}s are checked to determine how many measures
	 * are being added (usually one, but it's definitely possible
	 * and useful to add longer figures that comprise multiple measures.)
	 * 
	 * @param figures the figures to play
	 * @return this composer: allows calls to add to be chained
	 */
	public Composer add(Figure... figures) {
//		System.out.printf("measure %d\n", measure);
		// Add figures to composition, keeping track of where the
		// beat offsets occur
		double lastBeatOffsetUs = doAddFigures(this.measure, figures);
		
		// Based on start offsets, determine how many measures these
		// figures span
		Tempo tempo = composition.getTempo();
		double numMeasures = Math.floor(
				lastBeatOffsetUs / (tempo.getBeatsPerMeasure() * tempo.getUsPerBeat())) + 1;
		this.measure += ((int)numMeasures);
		
		return this;
	}
	
	/**
	 * Add figures to the composition at the current measure,
	 * and advance by exactly one measure.  This is useful if any
	 * of the figures play for slightly longer than one measure,
	 * but we don't want to allow the "spillover" as being counted
	 * as a full second measure.
	 * 
	 * @param figures the figures to play
	 * @return this composer: allows calls to add to be chained
	 */
	public Composer add1(Figure... figures) {
		doAddFigures(this.measure, figures);
		this.measure++;
		return this;
	}
	
	/**
	 * Add figures to play at a specified measure, without changing
	 * the current measure.  This is useful for scheduling figures
	 * to play at an absolute position in the composition.
	 * 
	 * @param measure the measure when the figures should play
	 * @param figures the figures to play
	 * @return this composer: allows calls to add to be chained
	 */
	public Composer at(int measure, Figure... figures) {
		doAddFigures(measure, figures);
		return this;
	}

	private double doAddFigures(int measure, Figure... figures) {
		Tempo tempo = composition.getTempo();
		double lastBeatOffsetUs = 0;
		for (Figure figure : figures) {
			PlayFigureEvent evt = new PlayFigureEvent();
			evt.setFigure(figure);
			evt.setStartUs(measure * tempo.getBeatsPerMeasure() * tempo.getUsPerBeat());
			composition.add(evt);
			for (Strike strike : figure.getRhythm()) {
				if (strike.getStartUs() > lastBeatOffsetUs) {
					lastBeatOffsetUs = strike.getStartUs();
				}
			}
		}
		return lastBeatOffsetUs;
	}
	
	/**
	 * Repeat given runnable specified number of times.
	 * 
	 * @param times number of times to repat
	 * @param r the runnable
	 */
	public void rpt(int times, Runnable r) {
		for (int i = 0; i < times; i++) {
			r.run();
		}
	}
	
	/**
	 * Audition by playing specified instrument live,
	 * recording the midi events, and translating them into a
	 * rhythm, melody, and figure.
	 * 
	 * @param instr the instrument to play live
	 */
	public void audition(Instrument instr) {
		this.audition = instr;
	}
	
	/**
	 * Set the volume (gain) on given instrument, effective
	 * from the beginning of the composition.
	 * 
	 * @param instr the instrument whose gain to set
	 * @param gain the gain, between 0 (silent) and 1 (loudest)
	 */
	public void v(Instrument instr, double gain) {
		composition.addGainEvent(0L, instr, gain);
	}
}
