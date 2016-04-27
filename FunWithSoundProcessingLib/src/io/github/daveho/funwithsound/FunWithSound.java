/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is distributed under the terms of the
 * <a href="http://opensource.org/licenses/Apache-2.0">Apache License 2.0</a>.
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package io.github.daveho.funwithsound;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import io.github.daveho.gervill4beads.Midi;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;

/**
 * FunWithSound Processing library.
 * 
 * @author David Hovemeyer
 */
public class FunWithSound {
	// Visualizations that can be turned on and off
	/** Piano keyboard. */
	public static final int PIANO = 0;
	// Other visualization constants will go here
	private static final int NUM_VISUALIZATIONS = PIANO+1; // Adjust this if more are added
	
	private static final int HIGH_NOTE = 108;
	private static final int LOW_NOTE = 21;

	private static final int BLACK_KEYS = 0x54A; // bits indicate black keys
	
	private static final int W = 11;
	private static final int H = 60;
	
	private static int totalWidth;
	static {
		// Determine the total width of the piano keyboard
		for (int note = LOW_NOTE, cyc = 9; note <= HIGH_NOTE; note++, cyc++) {
			if (((1 << (cyc%12)) & BLACK_KEYS) == 0) {
				// White key
				totalWidth += W;
			}
		}
	}
	
	/**
	 * Default start delay when saving a wave file
	 * (in microseconds.)
	 */
	public static final long DEFAULT_START_DELAY_US = 50000L; // 50 ms
	
	/**
	 * Default idle wait when saving a wave file
	 * (in microseconds.)
	 */
	public static final long DEFAULT_IDLE_WAIT_US = 2000000L; // 2 s
	
	/**
	 * Abstract base class for visualizations.
	 */
	private abstract class Visualization {
		private boolean enabled;

		/**
		 * Constructor.
		 * 
		 * @param enabled true if visualization is enabled by default,
		 *                false otherwise
		 */
		public Visualization(boolean enabled) {
			this.enabled = enabled;
		}
		
		/**
		 * @return true if the visualization is currently enabled
		 */
		public boolean isEnabled() {
			return this.enabled;
		}
		
		/**
		 * Enable or disable the visualization.
		 * 
		 * @param b true if the visualization should be enabled,
		 *          false if the visualization should be disabled
		 */
		public void setEnabled(boolean b) {
			this.enabled = b;
		}
		
		/**
		 * @return height of the visualization in pixels
		 */
		public abstract int getHeight();
		
		/**
		 * Downcall to handle a key press event.
		 * Does nothing by default.
		 * 
		 * @param key the key
		 */
		public void onKeyPress(char key) {
		}
		
		/**
		 * Downcall to handle a key release event.
		 * Does nothing by default.
		 * 
		 * @param key the key
		 */
		public void onKeyRelease(char key) {
		}
		
		/**
		 * Downcall to handle a draw event.
		 * Does nothing by default.
		 * 
		 * @param parent the parent {@link PApplet}
		 * @param y the y-coordinate of the top of the area where
		 *        the visualization should be drawn
		 */
		public void onDraw(PApplet parent, int y) {
			// Do nothing by default
		}
	}

	// Map of keys to offsets from current start note.
	//  e r   y u i    are the black keys
	// s d f g h j k l are the white keys
	private static final Map<Character, Integer> OFFSET_MAP = new HashMap<Character,Integer>();
	static {
		OFFSET_MAP.put('s', 0);
		OFFSET_MAP.put('e', 1);
		OFFSET_MAP.put('d', 2);
		OFFSET_MAP.put('r', 3);
		OFFSET_MAP.put('f', 4);
		OFFSET_MAP.put('g', 5);
		OFFSET_MAP.put('y', 6);
		OFFSET_MAP.put('h', 7);
		OFFSET_MAP.put('u', 8);
		OFFSET_MAP.put('j', 9);
		OFFSET_MAP.put('i', 10);
		OFFSET_MAP.put('k', 11);
		OFFSET_MAP.put('l', 12);
	}
	
	private class PianoVisualization extends Visualization {
		int startNote; // which note corresponds to the leftmost keyboard key
		BitSet noteOn;
		
		public PianoVisualization() {
			super(true); // is enabled by default
			startNote = 60;  // is always a C
			noteOn = new BitSet();
		}
		
		@Override
		public int getHeight() {
			return H;
		}
		
		@Override
		public void onKeyPress(char key) {
			if (OFFSET_MAP.containsKey(key)) {
				int note = startNote+OFFSET_MAP.get(key);
				if (note >= 0 && !noteOn.get(note)) {
					noteOn.set(note);
					if (receiver != null) {
						ShortMessage msg = Midi.createShortMessage(ShortMessage.NOTE_ON, note, 127);
						receiver.send(msg, player.getCurrentTimestamp());
					}
				}
			} else if (parent.key == PConstants.CODED && parent.keyCode == PConstants.UP) {
				attemptOctaveChange(1);
			} else if (parent.key == PConstants.CODED && parent.keyCode == PConstants.DOWN) {
				attemptOctaveChange(-1);
			}
		}
		
		@Override
		public void onKeyRelease(char key) {
			if (OFFSET_MAP.containsKey(key)) {
				int note = startNote+OFFSET_MAP.get(key);
				if (note >= 0 && noteOn.get(note)) {
					noteOn.clear(note);
					if (receiver != null) {
						ShortMessage msg = Midi.createShortMessage(ShortMessage.NOTE_OFF, note);
						receiver.send(msg, player.getCurrentTimestamp());
					}
				}
			}
		}
		
		@Override
		public void onDraw(PApplet parent, int y) {
			// Center the keyboard horizonally
			int xInit = (parent.width - totalWidth) / 2;
			
			int cyc;
			int x;
			
			parent.stroke(0);
			parent.strokeWeight(1.0f);
			
			// Draw the white keys first
			cyc = 9;  // cyc%12==0 means a C, note 21 is an A (9 half steps above C)
			x = xInit;
			for (int note = LOW_NOTE; note <= HIGH_NOTE; note++) {
				if (((1 << (cyc%12)) & BLACK_KEYS) == 0) {
					// White key
					if (noteOn.get(note)) {
						// Note is being played
						parent.fill(0,0,255);
					} else if (note >= startNote && note <= startNote+12) {
						// This note is in the active octave
						parent.fill(152,251,152);
					} else {
						parent.fill(255);
					}
					parent.rect(x, y, W, H);
					x += W;
				}
				
				cyc++;
			}
			
			// Then draw the black keys
			cyc = 9;
			x = xInit;
			for (int note = 21; note <= 108; note++) {
				if (((1 << (cyc%12)) & BLACK_KEYS) != 0) {
					// Black key
					if (noteOn.get(note)) {
						// Note is being played
						parent.fill(0,0,255);
					} else {
						parent.fill(0);
					}
					parent.rect(x-((W/2)-2)-1, y, W-2, (H*3)/5);
				} else {
					// White key
					x += W;
				}
				
				cyc++;
			}
		}
		
		private void attemptOctaveChange(int octave) {
			int nextStartNote = startNote + octave*12;
			if (nextStartNote < LOW_NOTE-12 || nextStartNote > HIGH_NOTE) {
				return;
			}
			
			// Don't update the octave if any notes are on
			if (!noteOn.isEmpty()) {
				return;
			}
			
			startNote = nextStartNote;
		}
	}
	
	PApplet parent;
	Player player;
	Receiver receiver;
	Visualization[] visualizations;
	Method onNoteEvent;
	ConcurrentLinkedQueue<NoteEvent> noteEventQueue;
	
	/**
	 * Constructor.
	 * 
	 * @param parent the parent {@link PApplet}
	 */
	public FunWithSound(PApplet parent) {
		this.parent = parent;
		
		this.visualizations = new Visualization[NUM_VISUALIZATIONS];
		this.visualizations[0] = new PianoVisualization();
		
		parent.registerMethod("dispose", this);
		parent.registerMethod("draw", this);
		parent.registerMethod("keyEvent", this);
		parent.registerMethod("post", this);
		
		this.onNoteEvent = findMethod("onNoteEvent", new Class<?>[]{NoteEvent.class});
		if (onNoteEvent != null) {
			noteEventQueue = new ConcurrentLinkedQueue<NoteEvent>();
		}
		
		System.out.println("Starting ##library.name## version ##library.prettyVersion##");
	}
	
	private Method findMethod(String name, Class<?>[] parameterTypes) {
		try {
			return parent.getClass().getMethod(name, parameterTypes);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Enable or disable a visualization.
	 * 
	 * @param visualization  the constant identifying the visualization to enable or disable
	 * @param b              true if the visualization should be enabled,
	 *                       false if the visualization should be disabled
	 */
	public void enableVis(int visualization, boolean b) {
		visualizations[visualization].setEnabled(b);
	}

	public void dispose() {
		if (player != null) {
			// Make sure player has stopped
			player.forceStopPlaying();
		}
	}
	
	public void draw() {
		if (noteEventQueue != null) {
			// If there are any NoteEvents, dispatch them to onNoteEvent
			while (!noteEventQueue.isEmpty()) {
				NoteEvent noteEvent = noteEventQueue.remove();
				try {
					onNoteEvent.invoke(parent, new Object[]{noteEvent});
				} catch (Exception e) {
					System.err.println("Error invoking onNoteEvent: " + e.getMessage());
				}
			}
		}
		
		int Y = 10;
		for (Visualization v : visualizations) {
			if (v.isEnabled()) {
				v.onDraw(parent, Y);
				Y += v.getHeight();
			}
		}
	}
	
	// Post-draw hook: see if playing has finished
	public void post() {
		if (player != null) {
			player.checkForEndOfPlaying();
		}
	}
	
	public void keyEvent(KeyEvent e) {
		if (e.getAction() == KeyEvent.PRESS) {
			onKeyPress();
		} else if (e.getAction() == KeyEvent.RELEASE) {
			onKeyRelease();
		}
	}
	
	private void onKeyPress() {
		char key = parent.key;
		
		for (Visualization v : visualizations) {
			if (v.isEnabled()) {
				v.onKeyPress(key);
			}
		}
	}

	private void onKeyRelease() {
		char key = parent.key;
		
		for (Visualization v : visualizations) {
			if (v.isEnabled()) {
				v.onKeyRelease(key);
			}
		}
	}
	

	public void play(Composer c) {
		// Make sure there isn't a player playing currently
		if (player != null) {
			player.checkForEndOfPlaying();
			if (player.isPlaying()) {
				return;
			} else {
				player = null;
				receiver = null;
			}
		}
		
		player = createPlayer();
		player.setComposition(c.getComposition());
		player.startPlaying();
	}
	
	public void saveWaveFile(Composer c, String fileName) {
		saveWaveFile(c, fileName, DEFAULT_START_DELAY_US, DEFAULT_IDLE_WAIT_US);
	}
	
	public void saveWaveFile(Composer c, String fileName, long startDelayUs, long idleWaitUs) {
		Player player = createPlayer();
		player.setComposition(c.getComposition());
		try {
			player.setStartDelayUs(startDelayUs);
			player.setIdleWaitUs(idleWaitUs);
			player.saveWaveFile(fileName);
		} catch (Exception e) {
			System.err.println("Couldn't save wave file: " + e.toString());
		}
	}

	protected Player createPlayer() {
		Player player = new Player() {
			@Override
			protected void prepareToPlay() throws MidiUnavailableException, IOException {
				super.prepareToPlay();
				
				// Get the MIDI Receiver so we can send MidiMessages
				// to the Gervill instance playing the live audition part
				// (if there is one)
				receiver = getReceiver();
			}
		};
		
		if (onNoteEvent != null) {
			player.setNoteEventCallback(new NoteEventCallback() {
				@Override
				public void onNoteEvent(NoteEvent noteEvent) {
					noteEventQueue.add(noteEvent);
				}
			});
		}
		
		return player;
	}
	
	/**
	 * @return true if the player is currently playing, false otherwise
	 */
	public boolean isPlaying() {
		if (player == null) {
			return false;
		}
		return player.isPlaying();
	}
}
