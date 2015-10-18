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

import java.util.ArrayList;

/**
 * Convert {@link Rhythm}, {@link Strike}, {@link Melody},
 * and {@link Chord} objects to code
 * comprised of calls to {@link Composer} methods.
 * These methods are used to convert live-played audition parts back
 * into code, but they could also be very useful for turning
 * algorithmically-created rhythms and melodies back into code.
 */
public class ConvertToCode {
	/**
	 * Convert a {@link Rhythm} to code in the form of a call to
	 * the {@link Composer} class's {@link Composer#r(Strike...)}
	 * method.
	 * 
	 * @param rhythm the {@link Rhythm}
	 * @param tempo the {@link Tempo}
	 * @return the code for the {@link Rhythm}
	 */
	public static String toCode(Rhythm rhythm, Tempo tempo) {
		StringBuilder buf = new StringBuilder();
		
		buf.append("r(\n");
		
		boolean first = true;
		
		for (Strike s : rhythm) {
			if (first) {
				first = false;
			} else {
				buf.append(",");
			}
			buf.append(toCode(s, tempo));
		}
		
		buf.append("\n)");
		
		return buf.toString();
	}
	
	/**
	 * Convert a {@link Strike} into code in the form of a call to the
	 * {@link Composer} class's {@link Composer#s(double, double, int)} method.
	 * 
	 * @param strike the {@link Strike}
	 * @param tempo the {@link Tempo}
	 * @return the code for the {@link Strike}
	 */
	public static String toCode(Strike strike, Tempo tempo) {
		StringBuilder buf = new StringBuilder();

		buf.append("s(");
		buf.append(String.format("%.3f", tempo.usToBeat((long)strike.getStartUs())));
		buf.append(",");
		buf.append(String.format("%.3f", tempo.usToBeat(((long)strike.getDurationUs()))));
		int vel = strike.getVelocity();
		if (vel < 127) {
			buf.append(",");
			buf.append(vel);
		}
		buf.append(")");

		return buf.toString();
	}
	
	/**
	 * Convert a {@link Melody} to code in the form of a call to
	 * the {@link Composer} class's {@link Composer#m(Object...)}
	 * method.
	 * 
	 * @param melody the {@link Melody}
	 * @param scale the {@link Scale}: if non-null, pitches that belong to the
	 *    scale will be generated as scale-relative notes rather than
	 *    absolute MIDI note numbers
	 * @return the code for the {@link Melody}
	 */
	public static String toCode(Melody melody, Scale scale) {
		StringBuilder buf = new StringBuilder();
		
		buf.append("m(\n");
		
		boolean first = true;
		
		for (Chord c : melody) {
			if (first) {
				first = false;
			} else {
				buf.append(",");
			}
			buf.append(toCode(c, scale));
		}
		
		buf.append("\n);\n");
		
		return buf.toString();
	}
	
	/**
	 * Convert a {@link Chord} to code in the form of a single
	 * integer (if the chord's single note is part of the scale),
	 * a call to the {@link Composer} class's {@link Composer#n(int...)}
	 * method (if all of the chord's multiple notes belong to the scale),
	 * or the {@link Composer} class's {@link Composer#an(int...)}
	 * method (if any of the chord's notes do not belong to the scale.)
	 * 
	 * @param c a {@link Chord}
	 * @param scale the {@link Scale} (can be null if there is no scale)
	 * @return the code for the {@link Chord}
	 */
	public static String toCode(Chord c, Scale scale) {
		StringBuilder buf = new StringBuilder();
		
		if (scale != null && allBelong(c, scale)) {
			// All of the chord's notes belong to the scale,
			// so we can specify a scale-relative note or chord
			if (c.size() == 1) {
				// Single note, so we can output it directly as an integer.
				buf.append(scale.findMidiNote(c.get(0)));
			} else {
				// Multi-note chord: output it using n()
				ArrayList<Integer> scaleRelativeNotes = new ArrayList<Integer>();
				for (Integer note : c) {
					scaleRelativeNotes.add(scale.findMidiNote(note));
				}
				buf.append("n(");
				buf.append(Util.join(scaleRelativeNotes, ","));
				buf.append(")");
			}
		} else {
			// No scale is specified, or there is at least one pitch
			// which isn't part of the scale, so output using an()
			buf.append("an(");
			buf.append(Util.join(c, ","));
			buf.append(")");
		}
		
		return buf.toString();
	}

	// See if all of the MIDI notes in the given
	// chord belong to the given scale.
	private static boolean allBelong(Chord c, Scale scale) {
		for (Integer note : c) {
			if (!scale.hasMidiNote(note)) {
				return false;
			}
		}
		return true;
	}
}
