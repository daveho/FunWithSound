package edu.ycp.cs.funwithsound;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A composition is a collection of {@link Figure}s specifying
 * rhythms and melodies to be played on specified instruments
 * at specified times.
 */
public class Composition implements Iterable<Figure> {
	private List<Figure> figures;
	
	public Composition() {
		figures = new ArrayList<Figure>();
	}
	
	public void add(Figure figure) {
		figures.add(figure);
	}
	
	public int size() {
		return figures.size();
	}
	
	public Figure get(int index) {
		return figures.get(index);
	}
	
	@Override
	public Iterator<Figure> iterator() {
		return figures.iterator();
	}
}
