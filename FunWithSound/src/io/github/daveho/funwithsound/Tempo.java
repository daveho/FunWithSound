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
 * Tempo determines the absolute tempo (beats per minute) and also,
 * based on the number of beats per measure, helps determine microsecond
 * offsets and durations of events (e.g., {@link Strike}s) occuring
 * within a measure.
 */
public class Tempo {
	private int beatsPerMinute;
	private int beatsPerMeasure;
	private long usPerBeat;
	
	/**
	 * Constructor.
	 * 
	 * @param beatsPerMinute number of beats per minute
	 * @param beatsPerMeasure number of beats per measure
	 */
	public Tempo(int beatsPerMinute, int beatsPerMeasure) {
		this.beatsPerMinute = beatsPerMinute;
		this.beatsPerMeasure = beatsPerMeasure;
		this.usPerBeat = (60L * 1000000L) / beatsPerMinute;
	}
	
	/**
	 * Get number of beats per minute.
	 * 
	 * @return number of beats per minute
	 */
	public int getBeatsPerMinute() {
		return beatsPerMinute;
	}
	
	/**
	 * Get number of beats per measure.
	 * 
	 * @return number of beats per measure
	 */
	public int getBeatsPerMeasure() {
		return beatsPerMeasure;
	}
	
	/**
	 * Get number of microseconds per beat.
	 * 
	 * @return number of microseconds per beat
	 */
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
	 * Convert a timestamp or duration in microseconds to a number
	 * of beats.
	 * 
	 * @param us timestamp or duration in microseconds
	 * @return equivalent number of beats
	 */
	public double usToBeat(long us) {
		return (double)us / (double)getUsPerBeat();
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
