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
import java.util.Iterator;
import java.util.List;

/**
 * A CompositeFigure is a group of one or more {@link Figures},
 * each of which could contain any number of {@link SimpleFigure}s.
 */
public class CompositeFigure implements Figure {
	private List<Figure> figures;
	
	/**
	 * Constructor.
	 */
	public CompositeFigure() {
		figures = new ArrayList<Figure>();
	}
	
	/**
	 * Add a child {@link Figure}.
	 * 
	 * @param child the child {@link Figure} to add
	 */
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
