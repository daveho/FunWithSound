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
		setScale(Scale.melodicMinor(69));
		setTempo(new Tempo(140, 8));
		
		Instrument vibes = new Instrument(12);
		
		Rhythm basic = r(s(0,1), s(1,1), s(2,1), s(3,1), s(4,1), s(5,1), s(6,1), s(7,1));
		Melody asc = m(n(0), n(1), n(2), n(3), n(4), n(5), n(6), n(0,1));
		
		at(1, basic, asc, vibes);
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
