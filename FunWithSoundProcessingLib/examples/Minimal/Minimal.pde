import io.github.daveho.funwithsound.*;

FunWithSound fws = new FunWithSound(this);

String SOUNDFONTS = "/some/dir/SoundFonts";               // Where the soundfonts are
String FLUID = SOUNDFONTS + "/fluid/FluidR3 GM2-2.SF2";   // Very good general-purpose soundfont

class MyComp extends Composer {
  void create() {
    // Composition goes here!
  }
}

MyComp c = new MyComp();

void setup() {
  size(600, 200);
  textSize(32);
  fill(0);
  text("Click to start playing", 125, 140);
  c.create();
}

void draw() {
}

void mouseClicked() {
  fws.play(c);
}