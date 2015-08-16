package io.github.daveho.funwithsound;

/**
 * An instrument plays notes/sounds as specified by
 * pairs of {@link Melody} and {@link Rhythm} objects.
 */
public class Instrument {
	private final InstrumentType type;
	private final int patch;
	private final String soundFont;
	
	Instrument(int patch) {
		this(InstrumentType.MIDI, patch, null);
	}
	
	Instrument(InstrumentType type, int patch, String soundFont) {
		this.type = type;
		this.patch = patch;
		this.soundFont = soundFont;
	}
	
	Instrument(InstrumentType type) {
		this(type, -1, null);
	}
	
	Instrument(String soundFont) {
		this(InstrumentType.MIDI_SOUNDFONT, -1, soundFont);
	}

	public InstrumentType getType() {
		return type;
	}
	
	public int getPatch() {
		return patch;
	}
	
	public String getSoundFont() {
		return soundFont;
	}

	public boolean hasSoundFont() {
		return soundFont != null;
	}
}
