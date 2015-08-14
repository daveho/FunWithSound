package io.github.daveho.funwithsound;

/**
 * An instrument plays notes/sounds as specified by
 * pairs of {@link Melody} and {@link Rhythm} objects.
 */
public class Instrument {
	private InstrumentType type;
	private int patch;
	
	Instrument(int patch) {
		this(InstrumentType.MIDI);
		this.patch = patch;
	}
	
	Instrument(InstrumentType type) {
		this.type = type;
	}
	
	public InstrumentType getType() {
		return type;
	}
	
	public int getPatch() {
		return patch;
	}
}
