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

import io.github.daveho.gervill4beads.GervillUGen;

import java.util.ArrayList;
import java.util.List;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;

/**
 * The GervillUGen and effects chain Beads for
 * a specific {@link Instrument}.
 */
class InstrumentInfo {
	/** GervillUGen, which sits in front of the effects chain. */
	GervillUGen gervill;
	
	/** Reference to the UGen currently at the end of the effects chain. */ 
	UGen endOfChain;

	/** Gain UGen (which is fed by the UGen at the end of the effects chain.) */
	Gain gain;
	
	List<GainEvent> gainEvents;
	
	public InstrumentInfo(GervillUGen gervill) {
		this.gervill = gervill;
		this.gainEvents = new ArrayList<GainEvent>();
		
		// Initially, the GervillUGen is at the end of the effects
		// chain (since there are no effects yet.)
		this.endOfChain = gervill;
	}
}