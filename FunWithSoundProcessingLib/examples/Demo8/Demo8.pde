// Processing version of Demo8
// Demonstrates the use of a custom instrument

import io.github.daveho.funwithsound.*;
import net.beadsproject.beads.core.*;
import net.beadsproject.beads.data.*;

FunWithSound fws = new FunWithSound(this) {
  protected Player createPlayer() {
    Player player = super.createPlayer();
    registerCustomInstruments(player);
    return player;
  }
};

final String SOUNDFONT_DIR = "/home/dhovemey/SoundFonts";
final String TR808 = SOUNDFONT_DIR + "/tr808/Roland_TR-808_batteria_elettronica.sf2";
final String HS_VDW = SOUNDFONT_DIR + "/hammersound/Vintage Dreams Waves v2.sf2";
final String ARACHNO = SOUNDFONT_DIR + "/arachno/Arachno SoundFont - Version 1.0.sf2";

class MyComp extends Composer {
  void create() {
    tempo(220, 8);
    
    Instrument tr808 = percussion(TR808);
    v(tr808, 0.9);
    
    Instrument vdwDrums = percussion(HS_VDW);
    v(vdwDrums, 0.7);
    
    Instrument arachnoDrums = percussion(ARACHNO);
    v(arachnoDrums, 0.9);
    
    Instrument lead = custom(0);
    
    addfx(lead, new AddAutoPan(.25, -.8, .8));
    
    DataBead delayParams = AddPingPongStereoDelays.defaultParams();
    delayParams.put(AddPingPongStereoDelays.NUM_DELAYS, 8);
    delayParams.put(AddPingPongStereoDelays.FIRST_DELAY_GAIN, .7);
    delayParams.put(AddPingPongStereoDelays.GAIN_DROP, .05);
    delayParams.put(AddPingPongStereoDelays.DELAY_MS, getComposition().getTempo().beatToUs(1.0)/1000.0);
    delayParams.put(AddPingPongStereoDelays.SPREAD, 0);
    addfx(lead, new AddPingPongStereoDelays(delayParams));
    addfx(lead, new AddReverb());
    v(lead, 0.15);
    
    // Good bass sounds: 4, 20, 21
    Instrument bass = instr(HS_VDW, 21);
    v(bass, 0.5);
    
    // Nice high percussive sounds: 22, 23, 25
    Instrument fun = instr(HS_VDW, 77);
    addfx(fun, new AddPingPongStereoDelays());
    v(fun, 0.5);
    
    Rhythm kick1r = r(p(0,127), p(1,110), p(2,127), p(5, 127), p(6.5, 110), p(7.5, 127));
    Melody kick1m = m(an(25), an(24), an(26), an(26), an(26), an(24));
    Figure kick1f = f(kick1r, kick1m, vdwDrums);
    
    Rhythm kick2r = r(p(0,127), p(1,110), p(2,127), p(5, 127), p(6.5, 110), p(7.5, 127));
    Melody kick2m = m(an(25), an(24), an(26), an(26), an(26), an(26));
    Figure kick2f = f(kick2r, kick2m, vdwDrums);
    
    Rhythm hihatr = gr(rr(p(2,127), .5,4), r(s(4,1.5,127), s(5.5,2,127)) );
    Melody hihatm = gm( rm(an(42), 4), m(an(49),an(49)) );
    Figure hihatf = f(hihatr, hihatm, tr808);
    
    Rhythm accentr = r(p(5,105));
    Melody accent1m = m(an(56));
    Melody accent2m = m(an(72));
    Figure accent1f = f(accentr, accent1m, vdwDrums);
    Figure accent2f = f(accentr, accent2m, arachnoDrums);
    
    Rhythm bass1r = r(
        s(0.000,0.5,102), s(0.5,1.5,90), s(2,0.5,106),
        s(5,0.5,99), s(5.5,0.5,102), s(7,0.5,99), s(7.5,0.5,106));
    Melody bass1m = m(
        an(38), an(40), an(40),
        an(45), an(43), an(45), an(43));
    Figure bass1f = f(bass1r, bass1m, bass);
    
    Rhythm bass2r = r(
        s(0.000,0.880,65), s(1.5,2,74), s(5,1,78), s(6,1,90));
    Melody bass2m = m(
        an(55), an(55), an(43), an(50));
    Figure bass2f = f(bass2r, bass2m, bass);

    Rhythm bass3r = sr(0, r(
          s(0.000,1,73), s(2,1,80), s(3,.5,65), s(3.5,.5,96), s(4,1.5,79)));
    Melody bass3m = m(
        an(47), an(50), an(52), an(50), an(52));
    Figure bass3f = f(bass3r, bass3m, bass);
    
    Rhythm bass4r = r(s(0,1,102), s(1,.5,106), s(1.5,1,106));
    Melody bass4m = m(an(38), an(38), an(40));
    Figure bass4f = f(bass4r, bass4m, bass);
    
    Rhythm lead1r = r(
        s(0.000,7.5,102),
        s(7.5,0.589,72), s(8,7.5,79),
        s(15.5,0.492,71), s(16,8,76),
        s(24,4,71), s(28,4,71));
    Melody lead1m = m(
        an(59),
        an(57), an(60),
        an(57), an(59),
        an(55), an(57));
    Melody lead2m = m(
        an(59),
        an(57), an(60),
        an(57), an(59),
        an(60), an(59));
    Figure lead1f = f(lead1r, lead1m, lead);
    Figure lead2f = f(lead1r, lead2m, lead);
    
    Rhythm lead3r = sr(0, r(
        s(0.000,1,96), s(1,1,71), s(2,1,58), s(3,2,102), s(5,3,74),
        s(8,0.545,102), s(8.5,0.464,87), s(9,6,69)));
    Melody lead3m = m(
        an(60), an(64), an(67), an(64), an(60),
        an(60), an(64), an(67));
    Figure lead3f = f(lead3r, lead3m, lead);
    
    Rhythm lead4r = r(
        s(0.000,1,96), s(1,0.617,73), s(1.5,.5,102), s(2,1,78), s(3,.5,76), s(3.5,4,102));
    Melody lead4m = m(
        an(60), an(62), an(60), an(62), an(50), an(52));
    Figure lead4f = f(lead4r, lead4m, lead);

    add1(gf(kick1f,hihatf));
    add1(gf(kick1f,hihatf));
    // Bass pattern starts
    add1(gf(kick1f,hihatf,bass1f));
    add1(gf(kick2f,hihatf,bass2f));
    add1(gf(kick1f,hihatf,bass1f));
    add1(gf(kick2f,hihatf,bass3f));
    // Percussion accents start
    add1(gf(kick1f,hihatf,bass4f,accent1f));
    add1(gf(kick1f,hihatf,accent2f));
    add1(gf(kick1f,hihatf,accent1f));
    add1(gf(kick1f,hihatf,accent2f));
    // Lead starts
    add1(gf(kick1f,hihatf,bass1f,accent1f,lead1f));
    add1(gf(kick2f,hihatf,bass2f,accent2f));
    add1(gf(kick1f,hihatf,bass1f,accent1f));
    add1(gf(kick2f,hihatf,bass3f,accent2f));
    add1(gf(kick1f,hihatf,bass1f,accent1f,lead2f));
    add1(gf(kick2f,hihatf,bass2f,accent2f));
    add1(gf(kick1f,hihatf,bass1f,accent1f));
    add1(gf(kick2f,hihatf,bass3f,accent2f));
    add1(gf(kick1f,hihatf,bass1f,accent1f,lead3f));
    add1(gf(kick2f,hihatf,bass2f,accent2f));
    add1(gf(kick1f,hihatf,bass1f,accent1f,lead4f));
    add1(gf(kick2f,hihatf,bass3f,accent2f));
    add1(gf(kick1f,hihatf,bass1f,accent1f,lead3f));
    add1(gf(kick2f,hihatf,bass2f,accent2f));
    add1(gf(kick1f,hihatf,bass1f,accent1f,lead4f));
    add1(gf(kick2f,hihatf,bass3f,accent2f));
    // Lead ends, hihats drop out, bass pattern is just bass4f
    add1(gf(kick1f,bass4f,accent1f));
    add1(gf(kick2f,bass4f,accent2f));
    add1(gf(kick1f,bass4f,accent1f));
    add1(gf(kick2f,bass4f,accent2f));
    // Hihats and full bass pattern return
    add1(gf(kick1f,hihatf,bass1f,accent1f));
    add1(gf(kick2f,hihatf,bass2f,accent2f));
    add1(gf(kick1f,hihatf,bass1f,accent1f));
    add1(gf(kick2f,hihatf,bass3f,accent2f));
    add1(gf(kick1f,hihatf,bass1f,accent1f));
    add1(gf(kick2f,hihatf,bass2f,accent2f));
    add1(gf(kick1f,hihatf,bass1f,accent1f));
    add1(gf(kick2f,hihatf,bass3f,accent2f));
    add1(gf(kick1f,hihatf,bass1f,accent1f));
    add1(gf(kick2f,hihatf,bass2f,accent2f));
    add1(gf(kick1f,hihatf,bass1f,accent1f));
    add1(gf(kick2f,hihatf,bass3f,accent2f));
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
}

void registerCustomInstruments(Player player) {
    CustomInstrumentFactoryImpl fac = new CustomInstrumentFactoryImpl(
      0, new CustomInstrumentFactoryImpl.CreateCustomInstrument() {
        public RealizedInstrument create(AudioContext ac) {
          DataBead params = Defaults.monosynthDefaults();
          params.putAll(Defaults.ringModulationVoiceDefaults());
          params.put(ParamNames.GLIDE_TIME_MS, 100);
          params.put(ParamNames.MIN_FREQ_MULTIPLE, -1);
          params.put(ParamNames.MAX_FREQ_MULTIPLE, 1);
          params.put(ParamNames.MOD_GLIDE_TIME_MS, 80);
          
          SynthToolkit tk = SynthToolkitBuilder.start()
              .withRingModulationVoice(Buffer.SAW, Buffer.SAW)
              .withASRNoteEnvelope()
              .getTk();
          
          MonoSynthUGen2 u = new MonoSynthUGen2(
              ac,
              tk,
              params,
              new double[]{1.0, 2.0},
              new double[]{.6, .2});
          return new RealizedInstrument(u, ac);
        }
      });
  player.setCustomInstrumentFactory(fac);
}
