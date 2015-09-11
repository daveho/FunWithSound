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
import io.github.daveho.gervill4beads.Midi;
import io.github.daveho.gervill4beads.ReceivedMidiMessageSource;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Receiver;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Gain;

/**
 * The synthesizer UGen and effects chain UGens for
 * a specific {@link Instrument}.
 */
public class InstrumentInfo {
	/** MidiMessages to be played by this instrument should be sent here. */
	public Receiver source;
	
	/**
	 * UGen at the head of the effects chain. MidiMessages sent to the
	 * source Receiver should be delivered (somehow) to this UGen.
	 */
	public UGen head;
	
	/** Reference to the UGen currently at the end of the effects chain. */ 
	public UGen tail;

	/** Gain UGen (which is fed by the UGen at the end of the effects chain.) */
	public Gain gain;
	
	List<GainEvent> gainEvents;
	
	/**
	 * Create an InstrumentInfo for a GervillUGen.
	 * 
	 * @param gervill the GervillUGen
	 */
	public InstrumentInfo(GervillUGen gervill) {
		this(gervill.getSynthRecv(), gervill);
	}
	
	/**
	 * "Generic" constructor for instruments that wish to have MIDI
	 * messages sequenced/delivered automatically by a
	 * ReceivedMidiMessageSource.  Most custom instruments will
	 * want to use this constructor.  The head UGen should
	 * override the <code>messageReceived</code> method
	 * and use the {@link Midi#hasMidiMessage(net.beadsproject.beads.core.Bead)}
	 * and {@link Midi#getMidiMessage(Bead)} methods to detect and
	 * process received MidiMessages.
	 * 
	 * @param head the UGen at the head of the effects chain
	 *             (i.e., the synthesizer)
	 * @param ac   the AudioContext
	 */
	public InstrumentInfo(UGen head, AudioContext ac) {
		this(new ReceivedMidiMessageSource(ac), head);
		
		// Deliver MidiMessages to the head UGen
		((ReceivedMidiMessageSource)source).addMessageListener(head);
	}

	/**
	 * "Generic" constructor: this is appropriate for custom instruments
	 * that define their own MIDI Receivers.
	 * 
	 * @param source the MIDI Receiver
	 * @param head   the UGen at the head of the effects chain
	 *               (i.e., the synthesizer or audio source)
	 */
	public InstrumentInfo(Receiver source, UGen head) {
		this.source = source;
		this.head = head;
		this.tail = head;
		this.gainEvents = new ArrayList<GainEvent>();
	}
}
