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

import io.github.daveho.gervill4beads.GervillUGen;
import io.github.daveho.gervill4beads.ReceivedMidiMessageSource;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Receiver;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;

/**
 * The GervillUGen and effects chain Beads for
 * a specific {@link Instrument}.
 */
class InstrumentInfo {
	/** MidiMessages to be played by this instrument should be sent here. */
	Receiver source;
	
	/**
	 * UGen at the head of the effects chain. MidiMessages sent to the
	 * source Receiver should be delivered (somehow) to this UGen.
	 */
	UGen head;
	
	/** Reference to the UGen currently at the end of the effects chain. */ 
	UGen tail;

	/** Gain UGen (which is fed by the UGen at the end of the effects chain.) */
	Gain gain;
	
	List<GainEvent> gainEvents;
	
	/**
	 * Create an InstrumentInfo for a GervillUGen.
	 * 
	 * @param gervill the GervillUGen
	 */
	public InstrumentInfo(GervillUGen gervill) {
		this.source = gervill.getSynthRecv();
		this.head = gervill;
		this.gainEvents = new ArrayList<GainEvent>();
		
		// Initially, the GervillUGen is at the end of the effects
		// chain (since there are no effects yet.)
		this.tail = gervill;
	}

	/**
	 * Create an InstrumentInfo for a @{link SampleBankUGen}.
	 * 
	 * @param sb the SampleBankUGen
	 * @param ac the AudioContext
	 */
	public InstrumentInfo(SampleBankUGen sb, AudioContext ac) {
		this.source = new ReceivedMidiMessageSource(ac);
		this.head = sb;
		this.gainEvents = new ArrayList<GainEvent>();
		this.tail = sb;
		
		// Deliver MidiMessages to the SampleBankUGen
		((ReceivedMidiMessageSource)source).addMessageListener(head);
	}
}
