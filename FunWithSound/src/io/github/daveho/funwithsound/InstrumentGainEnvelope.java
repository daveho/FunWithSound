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
import java.util.List;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;

/**
 * Precise gain envelope UGen used by {@link Player} to control
 * the gain of an {@link Instrument}.
 */
class InstrumentGainEnvelope extends UGen {
	private AudioContext ac;
	private List<GainEvent> gainEvents;
	private float gain;
	private long sampleCount;
	private int nextEvent;
	private float nextGain;
	private long effectiveSampleCount;
	
	/**
	 * Constructor.
	 * 
	 * @param ac the AudioContext
	 * @param gainEvents the list of {@link GainEvent}s
	 */
	public InstrumentGainEnvelope(AudioContext ac, List<GainEvent> gainEvents) {
		super(ac, 1);
		this.ac = ac;
		this.gainEvents = gainEvents;
		this.gain = 1.0f;
		this.nextEvent = 0;
		this.nextGain = 0.0f;
		this.effectiveSampleCount = Long.MAX_VALUE;
		findNextGainEvent();
	}

	@Override
	public void calculateBuffer() {
		// Fast path: if there are no more gain events
		// to be processed, or if the next effective timestamp
		// is in a later audio frame, just fill the output buffer
		// with the current gain.
		if (sampleCount + bufferSize <= effectiveSampleCount) {
			Arrays.fill(bufOut[0], gain);
			sampleCount += bufferSize;
			return;
		}
		
		// Slow path: the gain is changing at least once during this frame.
		long s = sampleCount;
		for (int i = 0; i < bufferSize; i++) {
			if (s >= effectiveSampleCount) {
				// GainEvent takes effect
				this.gain = this.nextGain;
				// Get ready for next GainEvent
				findNextGainEvent();
			}
			bufOut[0][i] = this.gain;
			s++;
		}
		sampleCount = s;
	}
	
	private void findNextGainEvent() {
		if (nextEvent >= gainEvents.size()) {
			//System.out.println("No more gain events?");
			// No more GainEvents to process
			effectiveSampleCount = Long.MAX_VALUE;
			return;
		}
		GainEvent e = gainEvents.get(nextEvent);
		// Determine sample number of given microsecond timestamp.
		this.effectiveSampleCount = (long)ac.msToSamples(e.ts / 1000L);
		this.nextGain = (float)e.gain; 
		//System.out.printf("Change gain to %.03f at sample %d\n", nextGain, effectiveSampleCount);
		this.nextEvent++;
	}
}