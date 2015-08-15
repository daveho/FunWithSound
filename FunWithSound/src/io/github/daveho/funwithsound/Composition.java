package io.github.daveho.funwithsound;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A composition is a collection of {@link PlayFigureEvent}s specifying
 * rhythms and melodies to be played on specified instruments
 * at specified times.
 */
public class Composition implements Iterable<PlayFigureEvent> {
	private List<PlayFigureEvent> PlayFigureEvents;
	private Scale scale;
	private Tempo tempo;
	
	public Composition() {
		PlayFigureEvents = new ArrayList<PlayFigureEvent>();
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
		PlayFigureEvents.add(PlayFigureEvent);
	}
	
	public int size() {
		return PlayFigureEvents.size();
	}
	
	public PlayFigureEvent get(int index) {
		return PlayFigureEvents.get(index);
	}
	
	@Override
	public Iterator<PlayFigureEvent> iterator() {
		return PlayFigureEvents.iterator();
	}
}
