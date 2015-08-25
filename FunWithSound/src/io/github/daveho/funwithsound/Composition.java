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

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A composition is a collection of {@link PlayFigureEvent}s specifying
 * rhythms and melodies to be played on specified instruments
 * at specified times.
 */
public class Composition implements Iterable<PlayFigureEvent> {
	private List<PlayFigureEvent> playFigureEvents;
	private Scale scale;
	private Tempo tempo;
	private List<GainEvent> gainEvents;
	private Map<Instrument, List<AddEffect>> fxMap;
	
	public Composition() {
		playFigureEvents = new ArrayList<PlayFigureEvent>();
		gainEvents = new ArrayList<GainEvent>();
		fxMap = new IdentityHashMap<Instrument, List<AddEffect>>();
	}
	
	public void setScale(Scale scale) {
		this.scale = scale;
	}
	
	public Scale getScale() {
		return scale;
	}
	
	public void setTempo(Tempo tempo) {
		this.tempo = tempo;
	}
	
	public Tempo getTempo() {
		return tempo;
	}
	
	public void add(PlayFigureEvent PlayFigureEvent) {
		playFigureEvents.add(PlayFigureEvent);
	}
	
	public int size() {
		return playFigureEvents.size();
	}
	
	public PlayFigureEvent get(int index) {
		return playFigureEvents.get(index);
	}
	
	@Override
	public Iterator<PlayFigureEvent> iterator() {
		return playFigureEvents.iterator();
	}

	public void addGainEvent(long ts, Instrument instr, double gain) {
		gainEvents.add(new GainEvent(ts, instr, gain));
	}
	
	public List<GainEvent> getGainEvents() {
		return gainEvents;
	}
	
	public Map<Instrument, List<AddEffect>> getEffectsMap() {
		return fxMap;
	}
	
	/**
	 * Add an effect to a specified {@link Instrument}.
	 * Each effect is appended onto the instrument's effects chain.
	 * 
	 * @param instr   the {@link Instrument}
	 * @param effect  the {@link AddEffect} which adds the effect
	 */
	public void addEffect(Instrument instr, AddEffect effect) {
		List<AddEffect> fx = fxMap.get(instr);
		if (fx == null) {
			fx = new ArrayList<AddEffect>();
			fxMap.put(instr, fx);
		}
		fx.add(effect);
	}
}
