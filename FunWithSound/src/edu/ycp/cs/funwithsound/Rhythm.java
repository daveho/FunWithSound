package edu.ycp.cs.funwithsound;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * A rhythm is a sequence of {@link Strike}s.
 * It specifies a rhythm pattern, but does not specify
 * notes or instruments.
 */
public class Rhythm implements Iterable<Strike> {
	private TreeSet<Strike> strikes;
	
	public Rhythm() {
		strikes = new TreeSet<Strike>(Strike.COMPARE_BY_START);
	}
	
	public void add(Strike strike) {
		strikes.add(strike);
	}
	
	public int size() {
		return strikes.size();
	}
	
	public Strike get(int index) {
		Iterator<Strike> i = strikes.iterator();
		int count = 0;
		while (i.hasNext()) {
			Strike s = i.next();
			if (count == index) {
				return s;
			}
			count++;
		}
		throw new IndexOutOfBoundsException();
	}
	
	@Override
	public Iterator<Strike> iterator() {
		return strikes.iterator();
	}
}
