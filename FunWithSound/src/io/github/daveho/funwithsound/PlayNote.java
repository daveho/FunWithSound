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

import javax.sound.midi.ShortMessage;

/**
 * Interface for objects such as {@link Voice}s and {@link NoteEnvelope}s
 * that play notes (and need to be notified of note on/note off events.)
 */
public interface PlayNote {
	/**
	 * Called when a note starts playing.
	 * 
	 * @param smsg the MIDI {@link ShortMessage} specifying the note on event
	 * @param note the MIDI note number
	 */
	public abstract void noteOn(ShortMessage smsg, int note);

	/**
	 * Called when a note stops playing.
	 * 
	 * @param smsg the MIDI {@link ShortMessage} specifying the note off event
	 * @param note the MIDI note number
	 */
	public abstract void noteOff(ShortMessage smsg, int note);

}