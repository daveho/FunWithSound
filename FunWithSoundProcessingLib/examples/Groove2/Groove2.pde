import io.github.daveho.funwithsound.*;
import net.beadsproject.beads.core.*;
import net.beadsproject.beads.data.*;

// You may need to change this, depending on which drive letter
// is assigned to your USB flash drive.
final String SOUNDFONT_DIR = "D:/SoundFonts";

// Some good percussion soundfonts
final String TR808 = SOUNDFONT_DIR + "/tr808/Roland_TR-808_batteria_elettronica.sf2";
final String TR909 = SOUNDFONT_DIR + "/tr909/Roland_TR-909_batteria_elettronica.sf2";
final String ARACHNO = SOUNDFONT_DIR + "/arachno/Arachno SoundFont - Version 1.0.sf2";
final String M1 = SOUNDFONT_DIR + "/m1/HS M1 Drums.sf2";
final String VDW = SOUNDFONT_DIR + "/hammersound/Vintage Dreams Waves v2.sf2";
final String MUSYNG = SOUNDFONT_DIR + "/musyng/Musyng Kite.sf2";
final String HS_PT1 = SOUNDFONT_DIR + "/hammersound/HS Pads and Textures I.sf2";
final String HS_PT2 = SOUNDFONT_DIR + "/hammersound/HS Pads and Textures II.sf2";
final String FLUID = SOUNDFONT_DIR + "/fluid/FluidR3 GM2-2.SF2";

class MyFWS extends FunWithSound {
  MyFWS(PApplet parent) {
    super(parent);
  }
  
  protected Player createPlayer() {
    Player player = super.createPlayer();
    registerCustomInstruments(player);
    return player;
  }

  public void saveWaveFile(MyComp c, String fileName) {
    Player player = createPlayer();
    player.setComposition(c.getComposition());
    try {
      player.setStartDelayUs(50000L);
      player.setIdleWaitUs(2000000L);
      player.saveWaveFile(fileName);
    } catch (Exception e) {
      println("Couldn't save wave file: " + e.toString());
    }
  }
}

MyFWS fws = new MyFWS(this);

class MyComp extends Composer {
  void create() {
    tempo(200, 8);  // 200 beats per minute, 8 beats per measure
    naturalMinor(57); // A minor, rooted at A3

    // Metronome instrument for keeping time
    Instrument metronome = percussion(TR808);

    // Set up a percussion instruments for building a rhythm pattern
    Instrument drumkit = percussion(FLUID);
    
    // Nice synth bass sound
    Instrument bass = instr(VDW, 4);
    v(bass,0.6); // make the bass a bit quieter
    
    // Pads
    Instrument pad = instr(FLUID, 96); // Pad 8 (sweep)
    Instrument pad2 = instr(FLUID, 95); // Pad 1 (halo) 

    // Lead
    Instrument lead = custom(0);
    v(lead, 0.1); // the raw waveforms are super loud, quiet it down 
    addfx(lead, new AddDelay(300.0, 1.0, 0.5));
    addfx(lead, new AddDelay(600.0, 1.0, 0.4));
    addfx(lead, new AddDelay(900.0, 1.0, 0.3));
    addfx(lead, new AddReverb());
    
    // Metronome pattern: plays a closed hihat every 2 beats
    Rhythm metr = rr(p(0,101), 2, 4);
    Figure metf = pf(metr, 42, metronome);
    
    Rhythm kickr = r(p(0,127), p(1,127),p(5,127),p(6.5,127));
    Melody kickm = m(an(36), an(36), an(36), an(36));
    Figure kickf = f(kickr, kickm, drumkit);
    
    Rhythm snarer = r(p(2,127), p(3.5,127), p(4.5, 127), p(6,127));
    Melody snarem = m(an(40),an(40),an(40),an(40));
    Figure snaref = f(snarer, snarem, drumkit);
    
    Rhythm hihatr = gr(
      rr(p(0,101), .5, 7),            // 7 sixteenth note strikes starting at 0
      rr(p(3.5,101), .25, 3),         // 3 thirty-second note strikes starting at 3.5
      r(s(4,1.5,101), s(5.5,2,101)),  // 2 open open hihat strikes at 4 and 5.5
      rr(p(7.5,101), .25, 2)          // 2 thirty-second note strikes starting at 7.5
    );
    Melody hihatm = gm(
      rm(an(42), 7),    // 7 closed hihat notes
      rm(an(42), 3),    // 3 closed hihat notes
      rm(an(46), 2),    // 2 open hihat notes
      rm(an(42), 2)     // 2 closed hihat notes
    );
    Figure hihatf = f(hihatr, hihatm, drumkit);
    
    Rhythm bassr = r(
      s(0,.5,101), s(.5,.5,101), s(1.5,1,101),
      s(5,.5,101), s(5.5,.5,101), s(6.5,1,101),
      s(8,.5,101), s(8.5,.5,101), s(9.5,1,101),
      s(11,.5,101), s(11.5,4,101)
    );
   /* Melody bassm = m(
      an(45), an(45), an(45),
      an(45), an(45), an(45),
      an(41), an(41), an(41),
      an(41),an(38));//*/
    Melody bassm = m(-7, -7, -7, -7, -7, -7, -9, -9, -9, -9, -11);
    Figure bassf = f(bassr, bassm, bass);
    
    Rhythm padr = r(
      s(0,6,101), s(6,1,101), s(7,1,101),
      s(8,4,101), s(12,2,101), s(14,2,101),
      s(16,6,101), s(22,1,101), s(23,1,101),
      s(24,4,101), s(28,2,101), s(30,2,101)
    );
    Melody padm = m(
      an(71), an(72), an(74),
      an(71), an(72), an(69),
      an(71), an(69), an(67),
      an(69), an(67), an(69));
    Figure padf = f(padr, padm, pad);
    
    Rhythm leadr = r(
      s(2,2,106), s(4,4,93),
      s(7,1,102), s(8,2,102), s(10,1,81), s(11,2,72), s(13,3,90),
      s(18,2,79), s(20,4,99),
      s(23,1,77), s(24,2,67), s(26,1,71), s(27,2,110), s(29,1,110), s(30,2,67),
      s(34,2,106), s(36,3,79),
      s(39,1,85), s(40,2,99), s(42,1,96), s(43,2,106), s(45,3,87),
      s(50,2,102), s(52,1,102), s(53,2,87),
      s(55,1,87), s(56,6,106)
      );
    Melody leadm = m(
      an(76), an(77),
      an(67), an(77), an(76), an(65), an(67),
      an(76), an(77),
      an(76), an(79), an(77), an(76), an(71), an(76),
      an(76), an(77),
      an(76), an(77), an(76), an(69), an(79),
      an(76), an(77), an(76),
      an(67), an(69)
    );
    Figure leadf = f(leadr, leadm, lead);
    
    Rhythm hihat2r = r(
      p(0,101), p(2,101), p(4,101), p(6,101)
    );
    Melody hihat2m = rm(an(42), 4);
    Figure hihat2f = f(hihat2r, hihat2m, drumkit);
    
    Rhythm hihat3r = r(
      p(0,101),
      p(1.5,101), p(2,101),
      p(3.5,101), p(4,101),
      p(5.5,101), p(6,101),
      s(6.5,1,101)
    );
    Melody hihat3m = m(
      an(42),
      an(44), an(42),
      an(44), an(42),
      an(44), an(42),
      an(46)
    );
    Figure hihat3f = f(hihat3r, hihat3m, drumkit);
    
    Rhythm hihat4r = r(
      p(0,101), p(1,101), p(2,101), p(3,101),
      s(4,.5,101),
      p(4.5,101), s(5,.5,101), p(5.5,101),
      p(6.5,101), s(7,.5, 101), p(7.5,101));
    Melody hihat4m = m(
      an(42), an(42), an(42), an(42),
      an(46),
      an(44), an(46), an(44),
      an(44), an(46), an(44)
    );
    Figure hihat4f = f(hihat4r, hihat4m, drumkit);
    
    Rhythm pad2r = r(
      s(0,4,127), s(4,.5,127), s(4.5,3.5,127),
      s(8,4,127), s(12,.5,127), s(12.5,3.5,127)
    );
    Melody pad2m = m(
      an(33),an(33),an(33),
      an(33),an(33),an(33)
    );
    Figure pad2f = f(pad2r, pad2m, pad2);
    
    Rhythm pad3r = r(
      s(0,4,127), s(4,4,127), s(8,4,127),
      s(12,.5,127), s(12.5,.5,127), s(13,3,127)
    );
    Melody pad3m = m(
      an(45), an(36), an(45),
      an(48), an(50), an(52));
    Figure pad3f = f(pad3r, pad3m, pad2);
    
    Rhythm kick2r = rr(p(0,127), 2, 4);
    Figure kick2f = pf(kick2r, 36, drumkit);
    
    Rhythm snare2r = r(p(2,127), p(5,127), p(6.5,127));
    Melody snare2m = m(an(40), an(39), an(39));
    Figure snare2f = f(snare2r, snare2m, drumkit);
    
    Rhythm lead2r = r(
      s(0,2,99), s(2,6,106),
      s(7,1,90), s(8,1,106), s(9,7,101),
      s(16,2,77), s(18,5,106),
      s(23,1,76), s(24,1,65), s(25,7,79)
    );
    Melody lead2m = m(
      an(72), an(74),
      an(72), an(74), an(76),
      an(84), an(76),
      an(77), an(81), an(79));
    Figure lead2f = f(lead2r, lead2m, lead);
    
    Rhythm bass2r = r(
      s(0,1,110), s(1,1,102), s(2,1,85), s(3,.5,106), s(3.5,1,102), s(4.5,0.5,102), s(5,1,102), s(6,1,85), s(7,1,106),
      s(8,1,110), s(9,1,102), s(10,1,85), s(11,.5,106), s(11.5,1,102), s(12.5,0.5,102), s(13,1,102), s(14,1,85), s(15,1,106)
      );
    Melody bass2m = m(
      an(55), an(57), an(67), an(57), an(67), an(65), an(64), an(55), an(57),
      an(55), an(57), an(67), an(57), an(67), an(65), an(64), an(55), an(57));
    Figure bass2f = f(bass2r, bass2m, bass);
    
    // The composition: which figures should start in each measure
    add1(gf(kickf,snaref,hihatf)); // percussion lead in
    add1(gf(kickf,snaref,hihatf));
    add1(gf(kickf,snaref,hihatf,bassf)); // bass enters
    add1(gf(kickf,snaref,hihatf));
    add1(gf(kickf,snaref,hihatf,bassf));
    add1(gf(kickf,snaref,hihatf));
    add1(gf(kickf,snaref,hihatf,bassf,padf)); // pad enters
    add1(gf(kickf,snaref,hihatf));
    add1(gf(kickf,snaref,hihatf,bassf));
    add1(gf(kickf,snaref,hihatf));
    add1(gf(kickf,snaref,hihatf,bassf)); // pad ends, and its just perc+bass
    add1(gf(kickf,snaref,hihatf));
    add1(gf(kickf,snaref,hihatf,bassf));
    add1(gf(kickf,snaref,hihatf));
    add1(gf(kickf,snaref,hihatf,bassf,padf,leadf)); // now everything, including lead!
    add1(gf(kickf,snaref,hihatf));
    add1(gf(kickf,snaref,hihatf,bassf));
    add1(gf(kickf,snaref,hihatf));
    add1(gf(kickf,snaref,hihatf,bassf,padf));
    add1(gf(kickf,snaref,hihatf));
    add1(gf(kickf,snaref,hihatf,bassf));
    add1(gf(kickf,snaref,hihatf));

    add1(gf(hihat2f,pad2f));
    add1(gf(hihat2f));
    add1(gf(hihat2f,pad2f));
    add1(gf(hihat2f));
    add1(gf(hihat3f,pad3f));
    add1(gf(hihat3f));
    add1(gf(hihat3f,pad3f));
    add1(gf(hihat3f));
    add1(gf(hihat4f,pad3f));
    add1(gf(hihat4f));
    add1(gf(hihat4f,pad3f));
    add1(gf(hihat4f));

    add1(gf(hihat4f,kick2f,pad3f));
    add1(gf(hihat4f,kick2f));
    add1(gf(hihat4f,kick2f,pad3f));
    add1(gf(hihat4f,kick2f));

    add1(gf(hihat4f,kick2f,snare2f,pad3f));
    add1(gf(hihat4f,kick2f,snare2f));
    add1(gf(hihat4f,kick2f,snare2f,pad3f));
    add1(gf(hihat4f,kick2f,snare2f));
  
    add1(gf(hihat4f,kick2f,snare2f,pad3f,lead2f));
    add1(gf(hihat4f,kick2f,snare2f));
    add1(gf(hihat4f,kick2f,snare2f,pad3f));
    add1(gf(hihat4f,kick2f,snare2f));
    add1(gf(hihat4f,kick2f,snare2f,pad3f,lead2f,bass2f));
    add1(gf(hihat4f,kick2f,snare2f));
    add1(gf(hihat4f,kick2f,snare2f,pad3f,bass2f));
    add1(gf(hihat4f,kick2f,snare2f));
    add1(gf(hihat4f,kick2f,snare2f,pad3f,lead2f,bass2f));
    add1(gf(hihat4f,kick2f,snare2f));
    add1(gf(hihat4f,kick2f,snare2f,pad3f,bass2f));
    add1(gf(hihat4f,kick2f,snare2f));
    add1(gf(hihat4f,kick2f,snare2f,pad3f,lead2f,bass2f));
    add1(gf(hihat4f,kick2f,snare2f));
    add1(gf(hihat4f,kick2f,snare2f,pad3f,bass2f));
    add1(gf(hihat4f,kick2f,snare2f));

    add1(gf(hihat4f,kick2f,pad2f));
    add1(gf(hihat4f,kick2f,pad2f));
    add1(gf(hihat4f,kick2f,pad2f));
    add1(gf(hihat4f,kick2f,pad2f));

    add1(gf(kick2f,pad2f));
    add1(gf(kick2f,pad2f));
    add1(gf(kick2f,pad2f));
    add1(gf(kick2f,pad2f));

    add1(gf(pad2f));
    add1(gf(pad2f));
    add1(gf(pad2f));
    add1(gf(pad2f));

    audition(bass);
  }
}

MyComp c = new MyComp();

void setup() {
  size(600,200);
  textSize(32);
  fill(0);
  text("Click to start playing", 125, 140); 
  c.create();
}


void draw() {
}

void mouseClicked() {
  fws.play(c);
  //fws.saveWaveFile(c, "/home/dhovemey/groove2.wav");
}

void registerCustomInstruments(Player player) {
  CustomInstrumentFactory factory = new CustomInstrumentFactoryImpl(
    0, new CustomInstrumentFactoryImpl.CreateCustomInstrument() {
      public RealizedInstrument create(AudioContext ac) {
        DataBead params = Defaults.monosynthDefaults();
        params.put(ParamNames.GLIDE_TIME_MS, 80.0f);
        SynthToolkit tk = SynthToolkitBuilder.start()
          .withWaveVoice(Buffer.SAW)
          .withASRNoteEnvelope()
          .getTk();
        MonoSynthUGen2 u = new MonoSynthUGen2(ac, tk, params,
          new double[]{ 1.0, 1.5, 2.0 },
          new double[]{ 1.0, .5, .4 }
          );
        return new RealizedInstrument(u, ac);
      }
    });
  player.setCustomInstrumentFactory(factory);
}
