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

import java.util.Arrays;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Function;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * Utility methods.
 */
public class Util {
	/**
	 * Compare two Long values.
	 * 
	 * @param left left value
	 * @param right right value
	 * @return negative if left &lt; right, positive if left &gt; right, 0 if they are equal
	 */
	public static int compareLongs(Long left, Long right) {
		return left.compareTo(right);
	}

	/**
	 * Compare two Integer values.
	 * 
	 * @param left left value
	 * @param right right value
	 * @return negative if left &lt; right, positive if left &gt; right, 0 if they are equal
	 */
	public static int compareInts(Integer left, Integer right) {
		return left.compareTo(right);
	}

	/**
	 * Create an array of <code>double</code> elements filled with specified value.
	 * 
	 * @param length the length of the array
	 * @param val the value to fill the array with
	 * @return the filled array
	 */
	public static double[] filledDoubleArray(int length, double val) {
		double[] a = new double[length];
		Arrays.fill(a, val);
		return a;
	}
	
	/**
	 * Create a UGen that generates a sine wave ranging between specified
	 * minimum and maximum values, oscillating at the specified frequency.
	 * 
	 * @param ac    the AudioContext
	 * @param min   the minimum value in the range
	 * @param max   the maximum value in the range
	 * @param freq  the oscillation frequency
	 * @return the UGen
	 */
	public static UGen getRangedSineFunction(AudioContext ac, final double min, double max, double freq) {
		WavePlayer sin = new WavePlayer(ac, (float)freq, Buffer.SINE);
		final float range = (float)(max - min);
		return new Function(sin) {
			@Override
			public float calculate() {
				float f = (x[0] + 1.0f) / 2.0f;
				return (float)(min + f*range);
			}
		};
	}
}
