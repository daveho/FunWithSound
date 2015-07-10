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
//		Rhythm basspat = r(s(0,2), s(4,1), s(5,1));
//		Melody bassmel = m(n(0,-4), n(0,-4), n(0,-3));
		Melody replow = m(n(0,-4),n(0,-4),n(0,-4),n(0,-4),n(0,-4),n(0,-4),n(0,-4),n(0,-4));
		Melody rephigh = m(n(3,-4),n(3,-4),n(3,-4),n(3,-4),n(3,-4),n(3,-4),n(3,-4),n(3,-4));
		
//		for (int i = 0; i < 4; i++) {
//			at(i, basspat, bassmel, bass);
//		}
		at(0, onbeat, replow, bass);
		at(1, onbeat, rephigh, bass);
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
