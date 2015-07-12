package edu.ycp.cs.funwithsound.demo;

import javax.sound.midi.MidiUnavailableException;

import edu.ycp.cs.funwithsound.Composer;
import edu.ycp.cs.funwithsound.Instrument;
import edu.ycp.cs.funwithsound.Melody;
import edu.ycp.cs.funwithsound.Player;
import edu.ycp.cs.funwithsound.Rhythm;
import edu.ycp.cs.funwithsound.Scale;
import edu.ycp.cs.funwithsound.Tempo;

public class Demo extends Composer {
	public void create() {
		setScale(Scale.major(65));
		setTempo(new Tempo(250, 8));
		
		Instrument bass = new Instrument(37);

		Rhythm onbeat = r(s(0, .5), s(1,.5), s(2,.5), s(3,.5), s(4,.5), s(5,.5), s(6,.5), s(7,.5));
		Melody low = m(-7, -6, -5, -4, -3, -2, -1, 0);
		Melody mid = m(0, 1, 2, 3, 4, 5, 6, 7);
		Melody hi = m(7, 8, 9, 10, 11, 12, 13, 14);
		
		at(0, onbeat, low, bass);
		at(1, onbeat, mid, bass);
		at(2, onbeat, hi, bass);
	}
	
	public void play() throws MidiUnavailableException {
		Player player = new Player();
		player.setComposition(getComposition());
		player.play();
	}
	
	public static void main(String[] args) throws MidiUnavailableException {
		Demo d = new Demo();
		d.create();
		d.play();
	}
}
