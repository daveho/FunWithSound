package io.github.daveho.funwithsound.demo;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import io.github.daveho.funwithsound.Composer;
import io.github.daveho.funwithsound.Player;

public abstract class DemoBase extends Composer {
	public abstract void create();

	public void play() throws MidiUnavailableException, IOException {
		Player player = new Player();
		player.setComposition(getComposition());
		player.play();
	}
}