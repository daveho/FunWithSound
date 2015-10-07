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
	private Instrument audition;
	
	/**
	 * Constructor.
	 */
	public Composition() {
		playFigureEvents = new ArrayList<PlayFigureEvent>();
		gainEvents = new ArrayList<GainEvent>();
		fxMap = new IdentityHashMap<Instrument, List<AddEffect>>();
	}
	
	/**
	 * Set the {@link Scale}.
	 * 
	 * @param scale the {@link Scale}
	 */
	public void setScale(Scale scale) {
		this.scale = scale;
	}
	
	/**
	 * Get the {@link Scale}.
	 * 
	 * @return the {@link Scale}
	 */
	public Scale getScale() {
		return scale;
	}
	
	/**
	 * Set the {@link Tempo}.
	 * 
	 * @param tempo the {@link Tempo}
	 */
	public void setTempo(Tempo tempo) {
		this.tempo = tempo;
	}
	
	/**
	 * Get the {@link Tempo}.
	 * 
	 * @return the {@link Tempo}
	 */
	public Tempo getTempo() {
		return tempo;
	}
	
	/**
	 * Add a {@link PlayFigureEvent}.
	 * 
	 * @param PlayFigureEvent the {@link PlayFigureEvent} to add
	 */
	public void add(PlayFigureEvent PlayFigureEvent) {
		playFigureEvents.add(PlayFigureEvent);
	}
	
	/**
	 * Get number of {@link PlayFigureEvent}s added.
	 * 
	 * @return number of {@link PlayFigureEvent}s
	 */
	public int size() {
		return playFigureEvents.size();
	}
	
	/**
	 * Get a {@link PlayFigureEvent}.
	 * 
	 * @param index the index of the {@link PlayFigureEvent}
	 * @return the {@link PlayFigureEvent}
	 */
	public PlayFigureEvent get(int index) {
		return playFigureEvents.get(index);
	}
	
	@Override
	public Iterator<PlayFigureEvent> iterator() {
		return playFigureEvents.iterator();
	}

	/**
	 * Add a {@link GainEvent}.
	 * 
	 * @param ts  the timestamp (in microseconds)
	 * @param instr the {@link Instrument} whose gain is being changed
	 * @param gain the gain to set
	 */
	public void addGainEvent(long ts, Instrument instr, double gain) {
		gainEvents.add(new GainEvent(ts, instr, gain));
	}
	
	/**
	 * Get the list of {@link GainEvent}s.
	 * 
	 * @return the list of {@link GainEvent}s
	 */
	public List<GainEvent> getGainEvents() {
		return gainEvents;
	}
	
	/**
	 * Get the map of {@link Instrument}s to lists of {@link AddEffect}
	 * objects specifying effects to add.
	 * 
	 * @return the effects map
	 */
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
	
	/**
	 * Get the audition instrument.
	 * 
	 * @return the audition instrument, or null if none
	 */
	protected  Instrument getAudition() {
		return audition;
	}

	/**
	 * Set the audition instrument.
	 * 
	 * @param instr the audition instrument to set
	 */
	public void setAudition(Instrument instr) {
		this.audition = instr;
	}
}
