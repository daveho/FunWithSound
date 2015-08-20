package io.github.daveho.funwithsound;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A CompositeFigure is a group of one or more {@link Figures},
 * each of which could contain any number of {@link SimpleFigure}s.
 */
public class CompositeFigure implements Figure {
	private List<Figure> figures;
	
	public CompositeFigure() {
		figures = new ArrayList<Figure>();
	}
	
	public void add(Figure child) {
		figures.add(child);
	}

	@Override
	public Iterator<SimpleFigure> iterator() {
		// Collect all SimpleFigures from all children
		List<SimpleFigure> result = new ArrayList<SimpleFigure>();
		for (Figure child : figures) {
			for (SimpleFigure sf : child) {
				result.add(sf);
			}
		}
		return result.iterator();
	}
}
