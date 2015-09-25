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

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.CombFilter;

/**
 * Add a flanger effect.
 * Accepts parameter configuration via a DataBead.
 * Note that parameters can only be set at effect creation time,
 * not during runtime.
 */
public class AddFlanger implements ParamNames, AddEffect {
	private DataBead params;
	
	/**
	 * Constructor.
	 * The default parameters will be used.
	 */
	public AddFlanger() {
		this.params = Defaults.flangerDefaults();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param params a DataBead containing the parameters
	 */
	public AddFlanger(DataBead params) {
		this.params = params;
	}

	@Override
	public UGen apply(AudioContext ac, RealizedInstrument info) {
		final int mindel = (int) ac.msToSamples(Util.getDouble(params, MIN_DELAY_MS));
		final int maxdel = (int) Math.ceil(ac.msToSamples(Util.getDouble(params, MAX_DELAY_MS)));
		
		CombFilter comb = new CombFilter(ac, maxdel);
		
		// Modulate the comb filter's delay with a sine function
		UGen delay = Util.rangedSineFunction(ac, mindel, maxdel, Util.getDouble(params, FREQ_HZ));
	
		comb.setDelay(delay);
		comb.setA(Util.getFloat(params, A));
		comb.setG(Util.getFloat(params, G));
		comb.setH(Util.getFloat(params, H));
		
		comb.addInput(info.tail);
		
		return comb;
	}

}
