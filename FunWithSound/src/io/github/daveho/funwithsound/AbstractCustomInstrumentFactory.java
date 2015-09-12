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

import java.util.HashMap;
import java.util.Map;

import net.beadsproject.beads.core.AudioContext;

/**
 * Convenient base class for {@link CustomInstrumentFactory}
 * implementations.
 */
public abstract class AbstractCustomInstrumentFactory implements CustomInstrumentFactory {
	/**
	 * Callback to create an {@link InstrumentInfo} for a custom instrument.
	 */
	public interface CreateCustomInstrument {
		/**
		 * Create an {@link InstrumentInfo} for a custom instrument.
		 * 
		 * @param ac the AudioContext
		 * @return the custom instrument {@link InstrumentInfo}
		 */
		public InstrumentInfo create(AudioContext ac);
	}
	
	private Map<Integer, CreateCustomInstrument> creatorMap;
	
	public AbstractCustomInstrumentFactory() {
		this.creatorMap = new HashMap<Integer, CreateCustomInstrument>();
	}
	
	/**
	 * Add a creator callback for a custom instrument.
	 * 
	 * @param code     the code of the custom instrument
	 * @param creator  the creator callback
	 */
	public void addCreator(int code, CreateCustomInstrument creator) {
		creatorMap.put(code, creator);
	}

	@Override
	public InstrumentInfo create(int code, AudioContext ac) {
		CreateCustomInstrument creator = creatorMap.get(code);
		if (creator == null) {
			throw new IllegalArgumentException("Don't know how to create custom instrument " + code);
		}
		return creator.create(ac);
	}

}
