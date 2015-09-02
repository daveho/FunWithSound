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

import java.util.HashMap;
import java.util.Map;

/**
 * An instrument plays notes/sounds as specified by
 * pairs of {@link Melody} and {@link Rhythm} objects.
 */
public class Instrument {
	private final InstrumentType type;
	private final int patch;
	private final String soundFont;
	private final Map<Integer, SampleInfo> sampleMap;
	
	Instrument(int patch) {
		this(InstrumentType.MIDI, patch, null);
	}
	
	Instrument(InstrumentType type, int patch, String soundFont) {
		this.type = type;
		this.patch = patch;
		this.soundFont = soundFont;
		this.sampleMap = new HashMap<Integer, SampleInfo>();
	}
	
	Instrument(InstrumentType type) {
		this(type, -1, null);
	}
	
	Instrument(String soundFont) {
		this(InstrumentType.MIDI, -1, soundFont);
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
	
	/**
	 * Add a sample to be played for given note.
	 * The entire sample will be played at full volume.
	 * 
	 * @param note the note
	 * @param fileName the filename of the sample
	 */
	public void addSample(int note, String fileName) {
		addSample(note, fileName, -1, -1, 1.0);
	}

	/**
	 * Add a sample to be played for given note.
	 * The specified range of the sample will be played at full volume.
	 * 
	 * @param note the note
	 * @param fileName the filename of the sample
	 * @param startMs the start time in milliseconds
	 * @param endMs the end time in milliseconds
	 */
	public void addSample(int note, String fileName, double startMs, double endMs) {
		addSample(note, fileName, startMs, endMs, 1.0);
	}

	/**
	 * Add sample to be played for given note.
	 * The entire sample will be played using the specified gain.
	 * 
	 * @param note the note
	 * @param fileName the sample filename
	 * @param gain the gain
	 */
	public void addSample(int note, String fileName, double gain) {
		addSample(note, fileName, -1, -1, gain);
	}

	/**
	 * Add sample to be played for given note.
	 * The specified range of the sample will be played with specified gain.
	 * 
	 * @param note the note
	 * @param fileName the sample filename
	 * @param startMs the start time
	 * @param endMs the end time
	 * @param gain the gain
	 */
	public void addSample(int note, String fileName, double startMs, double endMs, double gain) {
		if (type != InstrumentType.SAMPLE_BANK) {
			throw new RuntimeException("Can't add samples to " + type + " instrument");
		}
		sampleMap.put(note, new SampleInfo(note, fileName, startMs, endMs, gain));
	}
	
	public Map<Integer, SampleInfo> getSampleMap() {
		return sampleMap;
	}

	public boolean isMidi() {
		return type.isMidi();
	}
}
