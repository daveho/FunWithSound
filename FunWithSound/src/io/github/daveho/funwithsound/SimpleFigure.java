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

import java.util.Arrays;
import java.util.Iterator;

/**
 * A "simple" figure is a combination of {@link Rhythm}, {@link Melody}, and
 * {@link Instrument}.  It may be scheduled to be played at
 * a specified beat number (typically, the beginning of a measure).
 */
public class SimpleFigure implements Figure {
	private Rhythm rhythm;
	private Melody melody;
	private Instrument instrument;
	
	/**
	 * Constructor.
	 */
	public SimpleFigure() {
		
	}
	
	/**
	 * Set the {@link Rhythm}.
	 * 
	 * @param rhythm the {@link Rhythm} to set
	 */
	public void setRhythm(Rhythm rhythm) {
		this.rhythm = rhythm;
	}
	
	/**
	 * Get the {@link Rhythm}.
	 * 
	 * @return the {@link Rhythm}
	 */
	public Rhythm getRhythm() {
		return rhythm;
	}
	
	/**
	 * Set the {@link Melody}.
	 * 
	 * @param melody the {@link Melody} to set
	 */
	public void setMelody(Melody melody) {
		this.melody = melody;
	}
	
	/**
	 * Get the {@link Melody}.
	 * 
	 * @return the {@link Melody}
	 */
	public Melody getMelody() {
		return melody;
	}
	
	/**
	 * Set the {@link Instrument}.
	 * 
	 * @param instrument the {@link Instrument} to set
	 */
	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}
	
	/**
	 * Get the {@link Instrument}.
	 * 
	 * @return the {@link Instrument}
	 */
	public Instrument getInstrument() {
		return instrument;
	}

	@Override
	public Iterator<SimpleFigure> iterator() {
		return Arrays.asList(this).iterator();
	}
}
