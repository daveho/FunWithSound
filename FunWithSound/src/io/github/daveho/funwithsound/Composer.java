package io.github.daveho.funwithsound;

/**
 * The composer class builds upon the model classes to provide
 * a simple domain-specific language for defining compositions
 * consisting {@link Rhythm} and {@link Melody} objects
 * rendered using {@link Instrument}.  Note that the
 * {@link #setTempo(Tempo)} and {@link #setScale(Scale)}
 * methods must be called before any of the composition methods
 * can be used.  The composer produces a {@link Composition}
 * object as its product.
 */
public class Composer {
	private Tempo tempo;
	private Scale scale;
	private Composition composition;
	private int measure;
	
	public Composer() {
		composition = new Composition();
		measure = 0;
	}
	
	/**
	 * Create a new MIDI instrument.
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
	 * Get the composition.
	 * 
	 * @return the composition
	 */
	public Composition getComposition() {
		return composition;
	}
	
	/**
	 * Set the {@link Tempo} for the composition.
	 * 
	 * @param tempo the tempo
	 */
	public void setTempo(Tempo tempo) {
		this.tempo = tempo;
	}
	
	/**
	 * Set the {@link Scale} for the composition.
	 * 
	 * @param scale the scale
	 */
	public void setScale(Scale scale) {
		this.scale = scale;
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
			chord.add(scale.get(note));
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
	 * Create a chord with one or more notes, specifically for
	 * percussion.  The "notes" are actually percussion instruments:
	 * see <a href="http://www.midi.org/techspecs/gm1sound.php#percussion">General
	 * MIDI Level 1 Percussion Key Map</a>.  E.g., 35 is acoustic bass
	 * drum, etc.  Unlike the {@link #n(int...)} method, the "notes"
	 * are not mapped to a {@link Scale}, but instead are actual
	 * untranslated midi note numbers.
	 * 
	 * @param notes the "notes" specifying percussion instruments
	 * @return the chord
	 */
	public Chord pn(int... notes) {
		Chord chord = new Chord();
		for (int note : notes) {
			chord.add(note);
		}
		return chord;
	}

	/**
	 * Create a percussion "melody".  The notes/chords specify
	 * percussion instruments.  See
	 * <a href="http://www.midi.org/techspecs/gm1sound.php#percussion">General
	 * MIDI Level 1 Percussion Key Map</a>.
	 * 
	 * @param chords the notes/chords
	 * @return the percussion "melody"
	 */
	public Melody pm(Object... chords) {
		Melody melody = new Melody();
		for (Object chord : chords) {
			if (chord instanceof Number) {
				melody.add(pn(((Number)chord).intValue()));
			} else if (chord instanceof Chord) {
				melody.add((Chord)chord);
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
		strike.setStartUs(tempo.beatToUs(beat));
		strike.setDurationUs(tempo.beatToUs(duration));
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
		strike.setStartUs(tempo.beatToUs(beat));
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
		
		// Based on start offsets, determine how many measures these
		// figures span
		double numMeasures = Math.floor(
				lastBeatOffsetUs / (tempo.getBeatsPerMeasure() * tempo.getUsPerBeat())) + 1;
		measure += ((int)numMeasures);
		
		return this;
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
}
