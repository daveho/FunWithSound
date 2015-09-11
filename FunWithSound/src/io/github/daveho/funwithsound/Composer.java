// FunWithSound - A Java/Processing library for music composition
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

/**
 * The composer class builds upon the model classes to provide
 * a simple domain-specific language for defining compositions
 * consisting {@link Rhythm} and {@link Melody} objects
 * rendered using {@link Instrument}.  Note that the
 * tempo must be set before other methods are called.
 * Use {{@link #tempo(int, int)}} to set the tempo.
 * The default scale is E-flat major (rooted at MIDI note 51),
 * but you can change the scale by calling
 * {@link #major(int)}, {@link #naturalMinor(int)}, {@link #harmonicMinor(int)},
 * or {@link #melodicMinor(int)}.
 * The composer produces a {@link Composition}
 * object as its product.
 */
public class Composer {
	private Composition composition;
	private int measure;
	private double percussiveStrikeDuration;
	
	public Composer() {
		composition = new Composition();
		major(51); // Default scale is E flat major rooted in octave 3
		measure = 0;
		percussiveStrikeDuration = 0.25;
	}
	
	/**
	 * Set the duration of a percussive strike (created by the
	 * {@link #p(double)} and {@link #p(double, int)} methods.
	 * The default value is 0.25 (a quarter beat).  Note that the
	 * percussion sounds in some soundfonts don't play correctly
	 * if the strike duration is too short.
	 * 
	 * @param beats duration of percussive strikes, in beats
	 */
	public void pdur(double beats) {
		percussiveStrikeDuration = beats;
	}
	
	/**
	 * Return the current measure.
	 * 
	 * @return the current measure
	 */
	public int m() {
		return this.measure;
	}
	
	/**
	 * Create a new MIDI instrument using the built-in "emergency" MIDI
	 * soundbank.
	 * 
	 * @param patch the MIDI patch (starting from 1)
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
	 * @param patch the patch (starting from 1)
	 * @return the instrument
	 */
	public Instrument instr(String soundFont, int patch) {
		return new Instrument(InstrumentType.MIDI, patch, soundFont);
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
	 * Create a new custom instrument.
	 * 
	 * @param code the code identifying the type of custom instrument
	 * @return the instrument
	 */
	public Instrument custom(int code) {
		return new Instrument(InstrumentType.custom(code));
	}
	
	/**
	 * Create a sample player instrument.
	 * 
	 * @return a sample player instrument
	 */
	public Instrument samplePlayer() {
		return new Instrument(InstrumentType.SAMPLE_BANK);
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
		CompositeFigure result = new CompositeFigure();
		for (SimpleFigure sfo : orig) {
			SimpleFigure sf = new SimpleFigure();
			sf.setRhythm(sfo.getRhythm());
			sf.setMelody(xm(octave, sfo.getMelody()));
			sf.setInstrument(sfo.getInstrument());
			result.add(sf);
		}
		return result;
	}

	/**
	 * Combine multiple figures into a single figure.
	 * 
	 * @param figures the figures to combine
	 * @return the combined figure
	 */
	public Figure gf(Figure... figures) {
		CompositeFigure result = new CompositeFigure();
		for (Figure f : figures) {
			result.add(f);
		}
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
		return new Strike(
				composition.getTempo().beatToUs(beat),
				composition.getTempo().beatToUs(duration),
				velocity);
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
	 * @param velocity velocity, in the range 0..127
	 * @return the strike
	 */
	public Strike p(double beat, int velocity) {
		return s(beat, this.percussiveStrikeDuration, velocity);
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
	 * @param n    how many strikes to generate
	 * @return the rhythm
	 */
	public Rhythm rr(Strike first, double spacing, int n) {
		Rhythm result = new Rhythm();
		
		for (int i = 0; i < n; i++) {
			Strike s = new Strike(
					(long) (first.getStartUs() + (i*spacing*composition.getTempo().getUsPerBeat())),
					first.getDurationUs(),
					first.getVelocity());
			result.add(s);
		}
		
		return result;
	}
	
	/**
	 * Create a rhythm by combining the strikes in one or more
	 * Rhythms.
	 * 
	 * @param rhythms the rhythms to combine
	 * @return the single combined rhythm
	 */
	public Rhythm gr(Rhythm... rhythms) {
		Rhythm result = new Rhythm();
		for (Rhythm r : rhythms) {
			for (Strike s : r) {
				result.add(s);
			}
		}
		return result;
	}
	
	/**
	 * Create a rhythm by shifting the start beat of all of
	 * the strikes in a given rhythm by a specified shift.
	 * 
	 * @param shift   the shift (amount to add to each start beat)
	 * @param orig  the original rhythm
	 * @return the shifted rhythm
	 */
	public Rhythm sr(double shift, Rhythm orig) {
		Rhythm result = new Rhythm();
		for (Strike s : orig) {
			result.add(new Strike(
					s.getStartUs() + composition.getTempo().beatToUs(shift),
					s.getDurationUs(),
					s.getVelocity()));
		}
		return result;
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
		SimpleFigure result = new SimpleFigure();
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
	 * Add a figure to play in the current measure in
	 * the current {@link Composition}.  The start times of the
	 * {@link Strike}s are checked to determine how many measures
	 * are being added (usually one, but it's definitely possible
	 * and useful to add longer figures that comprise multiple measures.)
	 * 
	 * @param figure the figure to play
	 * @return this composer: allows calls to add to be chained
	 */
	public Composer add(Figure figure) {
//		System.out.printf("measure %d\n", measure);
		// Add figures to composition, keeping track of where the
		// beat offsets occur
		double lastBeatOffsetUs = doAddFigure(this.measure, figure);
		
		// Based on start offsets, determine how many measures these
		// figures span
		Tempo tempo = composition.getTempo();
		double numMeasures = Math.floor(
				lastBeatOffsetUs / (tempo.getBeatsPerMeasure() * tempo.getUsPerBeat())) + 1;
		this.measure += ((int)numMeasures);
		
		return this;
	}
	
	/**
	 * Repeatedly call {@link #add(Figure)} a specified number of times.
	 * 
	 * @param n       number of times to add the figure
	 * @param figure  the figure to add
	 * @return this composer: allows calls to be chained
	 */
	public Composer addn(int n, Figure figure) {
		for (int i = 0; i < n; i++) {
			add(figure);
		}
		return this;
	}
	
	/**
	 * Call the {@link #add(Figure)} method on a series of figures.
	 * This will add them to the composition sequentially, starting at
	 * the current measure.
	 * 
	 * @param figures the figures to play sequentially
	 * @return this composer: allows calls to be chained
	 */
	public Composer addseq(Figure... figures) {
		for (Figure f : figures) {
			add(f);
		}
		return this;
	}
	
	/**
	 * Repeatedly call {@link #addseq(Figure...)} a specified number of times.
	 * 
	 * @param n        number of times
	 * @param figures  the sequence of figures to repeatedly add
	 * @return this composer: allows calls to be chained
	 */
	public Composer addseqn(int n, Figure... figures) {
		for (int i = 0; i < n; i++) {
			addseq(figures);
		}
		return this;
	}
	
	/**
	 * Add a figure to the composition at the current measure,
	 * and advance by exactly one measure.  This is useful if
	 * the figure plays for slightly longer than one measure,
	 * but we don't want to allow the "spillover" as being counted
	 * as a full second measure.
	 * 
	 * @param figure the figure to play
	 * @return this composer: allows calls to add to be chained
	 */
	public Composer add1(Figure figure) {
		doAddFigure(this.measure, figure);
		this.measure++;
		return this;
	}
	
	/**
	 * Repeatedly call {@link #add1n(int, Figure)} a specified number of times.
	 * 
	 * @param n       number of times to add the figure
	 * @param figure  the figure to add
	 * @return this composer: allows calls to be chained
	 */
	public Composer add1n(int n, Figure figure) {
		for (int i = 0; i < n; i++) {
			add1(figure);
		}
		return this;
	}
	
	/**
	 * Add a figure to play at a specified measure, without changing
	 * the current measure.  This is useful for scheduling a figure
	 * to play at an absolute position in the composition.
	 * 
	 * @param measure the measure when the figures should play
	 * @param figure the figure to play
	 * @return this composer: allows calls to add to be chained
	 */
	public Composer at(int measure, Figure figure) {
		doAddFigure(measure, figure);
		return this;
	}

	private double doAddFigure(int measure, Figure figure) {
		Tempo tempo = composition.getTempo();
		double lastBeatOffsetUs = 0;
		for (SimpleFigure sf : figure) {
			PlayFigureEvent evt = new PlayFigureEvent();
			evt.setFigure(sf);
			evt.setStartUs(measure * tempo.getBeatsPerMeasure() * tempo.getUsPerBeat());
			composition.add(evt);
			for (Strike strike : sf.getRhythm()) {
				if (strike.getStartUs() > lastBeatOffsetUs) {
					lastBeatOffsetUs = strike.getStartUs();
				}
			}
		}
		return lastBeatOffsetUs;
	}
	
	/**
	 * Audition by playing specified instrument live,
	 * recording the midi events, and translating them into a
	 * rhythm, melody, and figure.
	 * 
	 * @param instr the instrument to play live
	 */
	public void audition(Instrument instr) {
//		this.audition = instr;
		composition.setAudition(instr);
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
	
	/**
	 * Set the volume (gain) on given instrument, effective
	 * from the beginning of the specified measure.
	 * 
	 * @param measure the measure when the volume change should take effect
	 * @param instr the instrument whose gain to set
	 * @param gain the gain, between 0 (silent) and 1 (loudest)
	 */
	public void v(int measure, Instrument instr, double gain) {
		int beat = measure * composition.getTempo().getBeatsPerMeasure();
		composition.addGainEvent(composition.getTempo().beatToUs(beat), instr, gain);
	}
	
	/**
	 * Add an effect to the effects chain for given instrument.
	 * 
	 * @param instr   the instrument
	 * @param effect  the effect to add
	 */
	public void addfx(Instrument instr, AddEffect effect) {
		composition.addEffect(instr, effect);
	}
}
