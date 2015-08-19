import io.github.daveho.funwithsound.*;

FunWithSound fws = new FunWithSound(this);

// Directory where you keep your soundfonts
String SOUNDFONTS = "/home/dhovemey/SoundFonts";

// Arachno is a really nice general MIDI soundfont
String ARACHNO = SOUNDFONTS + "/arachno/Arachno SoundFont - Version 1.0.sf2";

class MyComp extends Composer {
  public void create() {
    tempo(220, 8);
    melodicMinor(65);
    
    Instrument organ = instr(ARACHNO, 20);
    
    // This just plays a minor scale
    Rhythm tr = r(s(0, 1), s(1, 1), s(2, 1), s(3, 1), s(4, 1), s(5, 1), s(6, 1), s(7, 1));
    Melody tm = m(0, 1, 2, 3, 4, 5, 6, n(0, 7));
    Figure tf = f(tr, tm, organ);
    
    add1(tf);
    add1(xf(1, tf));
  }
}

MyComp c = new MyComp();

void setup() {
  size(400, 400);
  c.create();
  fws.play(c);
}

void draw() {
}