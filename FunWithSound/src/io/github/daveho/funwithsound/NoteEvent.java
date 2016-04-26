// Copyright 2015-2016, David Hovemeyer <david.hovemeyer@gmail.com>
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

import java.util.Comparator;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import io.github.daveho.gervill4beads.MidiMessageAndTimeStamp;

/**
 * Represention of a note on/off event to be dispatched
 * to a specified {@link Instrument}.
 * 
 * @author David Hovemeyer
 */
public class NoteEvent extends MidiMessageAndTimeStamp {
	/**
	 * Comparator for sorting {@link NoteEvent}s by timestamp.
	 * Note that <em>only</em> timestamps are compared, so
	 * events with the same timestamp will be placed in an unspecified
	 * order.
	 */
	public static final Comparator<? super NoteEvent> TIMESTAMP_COMPARATOR = new Comparator<NoteEvent>() {
		public int compare(NoteEvent o1, NoteEvent o2) {
			int cmp;
			
			cmp = ((Long)o1.timeStamp).compareTo((Long)o2.timeStamp);
			if (cmp != 0) {
				return cmp;
			}
			
			return 0;
		}
	};
	
	private Instrument instrument;

	/**
	 * Constructor.
	 * 
	 * @param msg          the MidiMessage specifying the note on/off event
	 * @param timeStamp    the timestamp in microseconds
	 * @param instrument   the {@link Instrument} to which the message will be sent
	 */
	public NoteEvent(MidiMessage msg, long timeStamp, Instrument instrument) {
		super(msg, timeStamp);
		this.instrument = instrument;
	}
	
	/**
	 * @return the {@link Instrument}
	 */
	public Instrument getInstrument() {
		return instrument;
	}
	
	/**
	 * @return true if this is a NOTE_ON event, false otherwise
	 */
	public boolean isNoteOn() {
		return (msg.getStatus() & 0xF0) == ShortMessage.NOTE_ON;
	}
	
	/**
	 * @return true if this is a NOTE_OFF event, false otherwise
	 */
	public boolean isNoteOff() {
		return (msg.getStatus() & 0xF0) == ShortMessage.NOTE_OFF;
	}
}
