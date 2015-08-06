package io.github.daveho.funwithsound;

/**
 * Tempo determines the absolute tempo (beats per minute) and also,
 * based on the number of beats per measure, helps determine microsecond
 * offsets and durations of events (e.g., {@link Strike}s) occuring
 * within a measure.
 */
public class Tempo {
	private int beatsPerMinute;
	private int beatsPerMeasure;
	private long usPerBeat;
	
	public Tempo(int beatsPerMinute, int beatsPerMeasure) {
		this.beatsPerMinute = beatsPerMinute;
		this.beatsPerMeasure = beatsPerMeasure;
		this.usPerBeat = (60L * 1000000L) / beatsPerMinute;
	}
	
	public int getBeatsPerMinute() {
		return beatsPerMinute;
	}
	
	public int getBeatsPerMeasure() {
		return beatsPerMeasure;
	}
	
	public long getUsPerBeat() {
		return usPerBeat;
	}
	
	/**
	 * Convert a beat number or count into an offset (within a measure)
	 * or duration in microseconds.
	 * 
	 * @param beat the beat number or count
	 * @return offset or duration in microseconds
	 */
	public long beatToUs(double beat) {
		return (long) (beat * usPerBeat);
	}
	
	/**
	 * Determine the offset in microseconds of the beginning of the specified
	 * measure.
	 * 
	 * @param measure the measure
	 * @return the microsecond timestamp of the start of the measure
	 */
	public long measureToUs(int measure) {
		return measure * beatsPerMeasure * usPerBeat;
	}
}
