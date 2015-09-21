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
import net.beadsproject.beads.data.DataBead;
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
	public static UGen rangedSineFunction(AudioContext ac, final double min, double max, double freq) {
		return rangedOscillator(ac, min, max, freq, Buffer.SINE);
	}

	/**
	 * Create a UGen that generates a waveform ranging between specified
	 * minimum and maximum values, oscillating at the specified frequency.
	 * 
	 * @param ac    the AudioContext
	 * @param min   the minimum value in the range
	 * @param max   the maximum value in the range
	 * @param freq  the oscillation frequency
	 * @param waveform the waveform (sine, square, etc.)
	 * @return the UGen
	 */
	public static UGen rangedOscillator(AudioContext ac, final double min, double max, double freq, Buffer waveform) {
		WavePlayer sin = new WavePlayer(ac, (float)freq, waveform);
		final float range = (float)(max - min);
		return new Function(sin) {
			@Override
			public float calculate() {
				float f = (x[0] + 1.0f) / 2.0f;
				return (float)(min + f*range);
			}
		};
	}
	
	/**
	 * Create a UGen that generates a sine wave ranging between specified
	 * minimum and maximum values, oscillating at the frequency
	 * specified by a UGen.
	 * 
	 * @param ac    the AudioContext
	 * @param min   the minimum value in the range
	 * @param max   the maximum value in the range
	 * @param freq  the oscillation frequency UGen
	 * @return the UGen
	 */
	public static UGen rangedSineFunction(AudioContext ac, final double min, double max, UGen freq) {
		Buffer waveform = Buffer.SINE;
		return rangedOscillator(ac, min, max, freq, waveform);
	}

	/**
	 * Create a UGen that generates a waveform ranging between specified
	 * minimum and maximum values, oscillating at the frequency
	 * specified by a UGen.
	 * 
	 * @param ac    the AudioContext
	 * @param min   the minimum value in the range
	 * @param max   the maximum value in the range
	 * @param freq  the oscillation frequency UGen
	 * @param waveform the waveform
	 * @return the UGen
	 */
	public static UGen rangedOscillator(AudioContext ac, final double min, double max, UGen freq, Buffer waveform) {
		WavePlayer sin = new WavePlayer(ac, freq, waveform);
		final float range = (float)(max - min);
		return new Function(sin) {
			@Override
			public float calculate() {
				float f = (x[0] + 1.0f) / 2.0f;
				return (float)(min + f*range);
			}
		};
	}
	
	/**
	 * Compute the multiplier needed to shift a frequency up
	 * or down by specified number of half steps. 
	 * 
	 * @param numHalfSteps number of half steps (negative to shift down)
	 * @return the multiplier
	 */
	public static double freqShift(double numHalfSteps) {
		return Math.pow(2, numHalfSteps/12);
	}

	/**
	 * Get a double parameter from a DataBead.
	 * Throws an exception if the stored parameter value can't
	 * be converted to a double.
	 * 
	 * @param params      the DataBead
	 * @param propName    the parameter name
	 * @return the double value
	 */
	public static double getDouble(DataBead params, String propName) {
		Number val = (Number) params.get(propName);
		return val.doubleValue();
	}

	/**
	 * Get a float parameter from a DataBead.
	 * Throws an exception if the stored parameter value can't
	 * be converted to a float.
	 * 
	 * @param params      the DataBead
	 * @param propName    the parameter name
	 * @return the float value
	 */
	public static float getFloat(DataBead params, String propName) {
		Number val = (Number) params.get(propName);
		return val.floatValue();
	}

	/**
	 * Get a float parameter from a DataBead.
	 * Throws an exception if the stored parameter value can't
	 * be converted to a float.
	 * Returns a specified default value if there is no stored
	 * parameter value.
	 * 
	 * @param params      the DataBead
	 * @param propName    the parameter name
	 * @param value       the default value
	 * @return the float value
	 */
	public static float getFloat(DataBead params, String propName, double value) {
		if (!params.containsKey(propName)) {
			return (float)value;
		}
		return getFloat(params, propName);
	}

	/**
	 * Get a float parameter from a DataBead.
	 * Throws an exception if the stored parameter value can't
	 * be converted to an int.
	 * 
	 * @param params     the DataBead
	 * @param propName   the parameter name
	 * @return the int value
	 */
	public static int getInt(DataBead params, String propName) {
		Number val = (Number) params.get(propName);
		return val.intValue();
	}

	/**
	 * Multiply output of given input UGen by given multiple.
	 * 
	 * @param input the input UGen
	 * @param fac the multiple
	 * @return UGen which multiplies the input UGen by the given multiple
	 */
	public static UGen multiply(UGen input, double fac) {
		final float f = (float)fac;
		return new Function(input) {
			@Override
			public float calculate() {
				return x[0] * f;
			}
		};
	}
}
