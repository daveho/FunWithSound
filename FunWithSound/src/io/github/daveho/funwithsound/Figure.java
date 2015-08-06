package io.github.daveho.funwithsound;

/**
 * A figure is a combination of {@link Rhythm}, {@link Melody}, and
 * {@link Instrument}.  It may be scheduled to be played at
 * a specified beat number (typically, the beginning of a measure).
 */
public class Figure {
	private Rhythm rhythm;
	private Melody melody;
	private Instrument instrument;
	
	public Figure() {
		
	}
	
	public void setRhythm(Rhythm rhythm) {
		this.rhythm = rhythm;
	}
	
	public Rhythm getRhythm() {
		return rhythm;
	}
	
	public void setMelody(Melody melody) {
		this.melody = melody;
	}
	
	public Melody getMelody() {
		return melody;
	}
	
	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}
	
	public Instrument getInstrument() {
		return instrument;
	}
}
