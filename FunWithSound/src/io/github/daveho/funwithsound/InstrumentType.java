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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Types of instruments.
 * This is a class that behaves like an enumeration:
 * all instances are unique.
 */
public class InstrumentType implements Comparable<InstrumentType> {
	/**
	 * Kinds of InstrumentTypes.
	 */
	public enum Kind {
		/** Standard MIDI instrument. */
		MIDI,
		
		/** MIDI percussion instrument. */
		MIDI_PERCUSSION,
		
		/** Sample bank instrument. */
		SAMPLE_BANK,
		
		/** Custom instrument type. */
		CUSTOM,;

		public boolean isMidi() {
			return this == MIDI || this == MIDI_PERCUSSION;
		}
	}
	
	private Kind kind;
	private int code;
	
	/** Standard MIDI instrument. */
	public static final InstrumentType MIDI = new InstrumentType(Kind.MIDI, 0);
	
	/** MIDI percussion instrument. */
	public static final InstrumentType MIDI_PERCUSSION = new InstrumentType(Kind.MIDI_PERCUSSION, 0);
	
	/**
	 * Sample bank instrument.
	 */
	public static final InstrumentType SAMPLE_BANK = new InstrumentType(Kind.SAMPLE_BANK, 0);
	
	private static final Map<Integer, InstrumentType> customInstrumentTypeMap =
			new ConcurrentHashMap<Integer, InstrumentType>();
	
	private InstrumentType(Kind kind, int code) {
		this.kind = kind;
		this.code = code;
	}

	/**
	 * Get the InstrumentType's kind.
	 * 
	 * @return
	 */
	public Kind getKind() {
		return kind;
	}

	/**
	 * Get integer code.  This is only meaningful for
	 * {@link Kind#CUSTOM} InstrumentTypes.
	 * 
	 * @return the integer code
	 */
	public int getCode() {
		return code;
	}
	
	@Override
	public int compareTo(InstrumentType o) {
		int cmp;
		cmp = this.kind.compareTo(o.kind);
		if (cmp != 0) {
			return cmp;
		}
		return code - o.code;
	}
	
	/**
	 * Get a {@link Kind#CUSTOM} InstrumentType with 
	 * specified code.  Note that these are interned,
	 * so for any given code, there is exactly one instance.
	 * 
	 * @param code the code
	 * @return the custom InstrumentType with the specified code
	 */
	public static InstrumentType custom(int code) {
		InstrumentType type = new InstrumentType(Kind.CUSTOM, code);
		InstrumentType prev = customInstrumentTypeMap.putIfAbsent(code, type);
		return prev != null ? prev : type;
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(kind);
		if (kind == Kind.CUSTOM) {
			buf.append("(");
			buf.append(code);
			buf.append(")");
		}
		return buf.toString();
	}

	/**
	 * Determine if this instrument type is a MIDI instrument type.
	 * 
	 * @return true if this is a MIDI instrument type, false otherwise
	 */
	public boolean isMidi() {
		return kind.isMidi();
	}

	/**
	 * Determine if this instrument type designates a custom instrument.
	 * 
	 * @return true if this is a custom instrument type, false otherwise
	 */
	public boolean isCustom() {
		return kind == Kind.CUSTOM;
	}
}
