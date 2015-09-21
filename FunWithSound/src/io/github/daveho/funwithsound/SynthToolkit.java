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

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.DataBead;

/**
 * Interface allowing the creation of synthesizer modules
 * such as {@link Voice}s and {@link NoteEnvelope}s.
 * Can be used with generic synthesizer implementations
 * such as {@link MonoSynthUGen2}.
 */
public interface SynthToolkit {
	/**
	 * Create a {@link Voice}.
	 * 
	 * @param ac the AudioContext
	 * @param freq the UGen controlling the frequency of the voice
	 * @param params the parameters
	 * @return the {@link Voice}
	 */
	public Voice createVoice(AudioContext ac, DataBead params, UGen freq);
	
	/**
	 * Create a {@link NoteEnvelope}.
	 * 
	 * @param ac the AudioContext
	 * @param params the parameters
	 * @param input the UGen whose output should be fed into the note envelope UGen
	 * @return the {@link NoteEnvelope}
	 */
	public NoteEnvelope createNoteEnvelope(AudioContext ac, DataBead params, UGen input);
}
