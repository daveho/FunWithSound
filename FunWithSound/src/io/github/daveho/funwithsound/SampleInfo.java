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
 * Information about a sample.
 */
public class SampleInfo {
	/** MIDI note number. */
	final int note;
	/** Sample filename. */
	final String fileName;
	/** Start time: if negative, then the entire sample will be played. */
	final double startMs;
	/** End time (ignored if start time is negative). */
	final double endMs;
	/**
	 * Static gain to be used for sample playback (note that this is independent of,
	 * and prior to, the sample player instrument's gain.)
	 */
	final double gain;
	
	/**
	 * Constructor.
	 * 
	 * @param note the MIDI note number
	 * @param fileName the sample filename
	 * @param startMs start time (if negative, then the entire sample will be played)
	 * @param endMs end time (ignored if start time is negative)
	 * @param gain the gain
	 */
	SampleInfo(int note, String fileName, double startMs, double endMs, double gain) {
		this.note = note;
		this.fileName = fileName;
		this.startMs = startMs;
		this.endMs = endMs;
		this.gain = gain;
	}
}
