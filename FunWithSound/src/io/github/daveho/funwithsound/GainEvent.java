package io.github.daveho.funwithsound;

/**
 * Event to set the volume (gain) of an {@link Instrument}
 * at a specified timestamp in a {@link Composition}.
 */
public class GainEvent {
	public final long ts;
	public final Instrument instr;
	public final double gain;
	
	public GainEvent(long ts, Instrument instr, double gain) {
		if (gain < 0.0 || gain > 1.0) {
			throw new IllegalArgumentException("invalid gain: " + gain + " (must be between 0 and 1)");
		}
		this.ts = ts;
		this.instr = instr;
		this.gain = gain;
	}
}
