import io.github.daveho.funwithsound.*;

FunWithSound fws = new FunWithSound(this);

class MyComp extends Composer {
  void create() {
    tempo(150, 8);

    Instrument drumkit = percussion();
    Instrument synth = instr(95);

    // Create a simple hihat rhythm figure
    Rhythm hihatr = rr(p(0), 1, 8);
    Figure hihatf = pf(hihatr, 42, drumkit);

    // play hihats for 8 measures
    add1n(8, hihatf);

    // play the synth part live
    audition(synth);
  }
}

MyComp c = new MyComp();

void setup() {
  background(235);
  size(600,200);
  c.create();
}

void draw() {
  textSize(24);
  text("Click to start playback", 160, 130);
}

void mouseClicked() {
  fws.play(c);
}