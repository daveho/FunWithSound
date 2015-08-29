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
	private final Map<Integer, String> sampleMap;
	
	Instrument(int patch) {
		this(InstrumentType.MIDI, patch, null);
	}
	
	Instrument(InstrumentType type, int patch, String soundFont) {
		this.type = type;
		this.patch = patch;
		this.soundFont = soundFont;
		this.sampleMap = new HashMap<Integer, String>();
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
	
	public void addSample(int note, String fileName) {
		if (type != InstrumentType.SAMPLE_BANK) {
			throw new RuntimeException("Can't add samples to " + type + " instrument");
		}
		sampleMap.put(note, fileName);
	}
	
	public Map<Integer, String> getSampleMap() {
		return sampleMap;
	}

	public boolean isMidi() {
		return type.isMidi();
	}
}
