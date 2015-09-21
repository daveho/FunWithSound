package io.github.daveho.funwithsound;

import javax.sound.midi.ShortMessage;

/**
 * Interface for objects such as {@link Voice}s and {@link NoteEnvelope}s
 * that play notes (and need to be notified of note on/note off events.)
 */
public interface PlayNote {

	public abstract void noteOn(ShortMessage smsg, int note);

	public abstract void noteOff(ShortMessage smsg, int note);

}