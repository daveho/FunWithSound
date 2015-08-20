import io.github.daveho.funwithsound.*;

FunWithSound fws = new FunWithSound(this);

class MyComp extends Composer {
  void create() {
    tempo(200, 8);
    major(51); //  E flat major
    
    Instrument organ = instr(19);
    
    Rhythm tr = r(
      s(0,2), s(2,1), s(3,1), s(4,1),
      s(6, 2), s(8,2), s(10,2), s(12,2), s(14, 2), s(16,2),
      s(18,1), s(19,1), s(20,1), s(22,2), s(24,2),
      s(26,2), s(28,2)
    );
    Melody tm = m(
      4, 7, 8, 9,
      9, 8, 10, 9, 4, 3,
      9, 7, 8, 3, 2,
      6, 7
    );
    Figure tf = f(tr, tm, organ);
    
    add1(tf);
  }
}

MyComp c = new MyComp();

void setup() {
  size(400,400);
  c.create();
  fws.play(c);
}

void draw() {
}
