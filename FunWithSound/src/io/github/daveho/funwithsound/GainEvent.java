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
