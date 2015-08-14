package io.github.daveho.funwithsound.demo;

import javax.sound.midi.MidiUnavailableException;

import io.github.daveho.funwithsound.Composer;
import io.github.daveho.funwithsound.Player;

public abstract class DemoBase extends Composer {
	public abstract void create();

	public void play() throws MidiUnavailableException {
		Player player = new Player();
		player.setComposition(getComposition());
		player.play();
	}

}