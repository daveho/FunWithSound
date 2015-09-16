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
import net.beadsproject.beads.ugens.Panner;

/**
 * Add a static pan effect.
 */
public class AddStaticPan implements AddEffect {
	private double pos;

	/**
	 * Constructor.
	 * 
	 * @param pos position (-1 full left, 0 center, 1 full right)
	 */
	public AddStaticPan(double pos) {
		this.pos = pos;
	}

	@Override
	public UGen apply(AudioContext ac, RealizedInstrument info) {
		Panner panner = new Panner(ac, (float)pos);
		
		panner.addInput(info.tail);
		
		return panner;
	}
}
