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
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Panner;

/**
 * Add "ping-pong" stereo delay, where the delayed sound
 * bounces between the left and right channels.
 * Accepts parameter configuration via a DataBead.
 */
public class AddPingPongStereoDelays implements ParamNames, AddEffect {

	/**
	 * Get the default parameters.
	 * @return the default parameters
	 * @deprecated Use {@link Defaults#pingPongStereoDelayDefaults()} instead
	 */
	public static DataBead defaultParams() {
		return Defaults.pingPongStereoDelayDefaults();
	}
	
	private DataBead params;
	
	/**
	 * Constructor using default parameters.
	 */
	public AddPingPongStereoDelays() {
		params = Defaults.pingPongStereoDelayDefaults();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param params parameters
	 */
	public AddPingPongStereoDelays(DataBead params) {
		this.params = params.clone();
	}
	
	@Override
	public UGen apply(AudioContext ac, RealizedInstrument info) {
		int numDelays = Util.getInt(params, NUM_DELAYS);
		
		
		// A mixer to combine the original signal and the delayed signals
		Gain mixer = new Gain(ac, 2);
		
		// Pass the original signal straight through to the mixer
		mixer.addInput(info.tail);
		
		float firstDelayGain = Util.getFloat(params, FIRST_DELAY_GAIN);
		float gainDrop = Util.getFloat(params, GAIN_DROP);
		float spread = Util.getFloat(params, SPREAD);
		
		// Create the delays using CombFilters and Panners.
		// The CombFilters are configured not to pass any of the
		// original signal through.
		for (int i = 0; i < numDelays; i++) {
			double delayMs = Math.ceil(Util.getDouble(params, DELAY_MS) * (i+1));
			int delaySamples = (int) Math.ceil(ac.msToSamples(delayMs));
			//System.out.printf("Delay by %d samples\n", delaySamples);
			CombFilter cf = new CombFilter(ac, delaySamples);
			cf.setDelay(delaySamples);
			cf.setA(0.0f);
			float curGain = firstDelayGain - i*gainDrop;
			//System.out.printf("Set delay gain to %f\n", curGain);
			cf.setG(curGain);
			cf.setH(0.0f);
			cf.addInput(info.tail);
			
			Panner panner = new Panner(ac);
			panner.setPos((i%2 == 0) ? -spread : spread); 
			panner.addInput(cf);
			
			mixer.addInput(panner);
		}
		
		return mixer;
	}

}
