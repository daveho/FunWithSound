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

/**
 * Interface for objects that add an effect to an
 * {@link InstrumentInfo}'s effects chain.
 */
public interface AddEffect {
	/**
	 * Update the given {@link InstrumentInfo} to add an effect.
	 * 
	 * @param ac   the AudioContext
	 * @param info the {@link InstrumentInfo}
	 * @return the UGen that is the output of the effect: this will become
	 *         the new end of the effects chain
	 */
	public UGen apply(AudioContext ac, InstrumentInfo info);
}
