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
 * Convenient implementation of {@link CustomInstrumentFactory}.
 * Maintains a map of custom instrument codes to creator callbacks.
 */
public class CustomInstrumentFactoryImpl implements CustomInstrumentFactory {
	/**
	 * Callback to create an {@link RealizedInstrument} for a custom instrument.
	 */
	public interface CreateCustomInstrument {
		/**
		 * Create an {@link RealizedInstrument} for a custom instrument.
		 * 
		 * @param ac the AudioContext
		 * @return the custom instrument {@link RealizedInstrument}
		 */
		public RealizedInstrument create(AudioContext ac);
	}
	
	private Map<Integer, CreateCustomInstrument> creatorMap;
	
	/**
	 * Constructor.  The arguments should be a sequence of pairs
	 * of <code>Integer,CreateCustomInstrument</code> specifying custom
	 * instrument codes and the creator callbacks for those codes.
	 */
	public CustomInstrumentFactoryImpl(Object... pairs) {
		this.creatorMap = new HashMap<Integer, CreateCustomInstrument>();
		if (pairs.length % 2 != 0) {
			throw new IllegalArgumentException("There should be an even number of arguments");
		}
		for (int i = 0; i < pairs.length; i += 2) {
			Integer code = (Integer) pairs[i];
			CreateCustomInstrument creator = (CreateCustomInstrument) pairs[i+1];
			addCreator(code, creator);
		}
	}
	
	/**
	 * Add a creator callback for a custom instrument.
	 * (It is generally not necessary to call this method directly
	 * if all creator callbacks for all custom instruments have been
	 * registered by the constructor.)
	 * 
	 * @param code     the code of the custom instrument
	 * @param creator  the creator callback
	 */
	public void addCreator(int code, CreateCustomInstrument creator) {
		creatorMap.put(code, creator);
	}

	@Override
	public RealizedInstrument create(int code, AudioContext ac) {
		CreateCustomInstrument creator = creatorMap.get(code);
		if (creator == null) {
			throw new IllegalArgumentException("Don't know how to create custom instrument " + code);
		}
		return creator.create(ac);
	}

}
