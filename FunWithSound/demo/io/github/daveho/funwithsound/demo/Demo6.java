package io.github.daveho.funwithsound.demo;

import io.github.daveho.funwithsound.AddPingPongStereoDelays;
import io.github.daveho.funwithsound.CustomInstrumentFactoryImpl;
import io.github.daveho.funwithsound.AddDelay;
import io.github.daveho.funwithsound.AddFlanger;
import io.github.daveho.funwithsound.AddOscillatingBandPassFilter;
import io.github.daveho.funwithsound.AddReverb;
import io.github.daveho.funwithsound.BandpassFilterMonoSynthUGen;
import io.github.daveho.funwithsound.CustomInstrumentFactory;
import io.github.daveho.funwithsound.Figure;
import io.github.daveho.funwithsound.Instrument;
import io.github.daveho.funwithsound.RealizedInstrument;
import io.github.daveho.funwithsound.Melody;
import io.github.daveho.funwithsound.MonoSynthUGen;
import io.github.daveho.funwithsound.Player;
import io.github.daveho.funwithsound.Rhythm;
import io.github.daveho.funwithsound.Util;

import java.io.IOException;

import javax.sound.midi.MidiUnavailableException;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.DataBead;

public class Demo6 extends DemoBase {
	@Override
	public void create() {
		tempo(120, 8);
		
		Instrument tr808 = percussion(TR808);
		
		Instrument monosynth = custom(0);
		v(monosynth, 0.4);
		addfx(monosynth, new AddFlanger());
		for (int i = 0; i < 8; i++) {
			addfx(monosynth, new AddDelay(i*100, 1.0, 0.3));
		}
		
		Instrument monosynth2 = custom(1);
		v(monosynth2, 0.4);
//		DataBead params2 = AddFlanger.defaultParams();
//		params2.put(AddFlanger.MIN_DELAY_MS, 0);
//		params2.put(AddFlanger.MAX_DELAY_MS, 5);
//		params2.put(AddFlanger.FREQ_HZ, 1.0);
//		addfx(monosynth2, new AddFlanger(params2));
		DataBead params = AddPingPongStereoDelays.defaultParams();
		params.put(AddPingPongStereoDelays.DELAY_MS, 500);
		params.put(AddPingPongStereoDelays.NUM_DELAYS, 8);
		params.put(AddPingPongStereoDelays.FIRST_DELAY_GAIN, 0.6);
		params.put(AddPingPongStereoDelays.SPREAD, 0.9);
		addfx(monosynth2, new AddPingPongStereoDelays(params));
		addfx(monosynth2, new AddOscillatingBandPassFilter(200, 1200, 0.125));
		addfx(monosynth2, new AddReverb());
		
//		Rhythm clickr = rr(s(0,.5,101), 1, 8);
//		Figure clickf = pf(clickr, 42, tr909);
		
		Rhythm hihatr = r(s(0,3,127), s(3.5,.5,101), s(4,3,127));
		Figure hihatf = pf(hihatr, 46, tr808);
		
		Rhythm stickr = r(p(0,127), p(2.5,127), p(3,127));
		Figure stickf = pf(stickr, 37, tr808);
		
		Rhythm drumr = r(s(1,.5,101), s(1.5,.5,127));
		Figure drumf = pf(drumr, 40, tr808);
		
//		Rhythm hihat_fill1r = rr(p(2,80), .25, 4);
//		Figure hihat_fill1f = pf(hihat_fill1r, 42, tr909);
//		Rhythm hihat_fill2r = rr(p(3,90), .125, 8);
//		Figure hihat_fill2f = pf(hihat_fill2r, 42, tr909);
//		
//		Rhythm clapr = r(p(6,127), p(6.5,127), p(7,127));
//		Melody clapm = m(an(77), an(76), an(77));
//		Figure clapf = f(clapr, clapm, tr909);
		
		Rhythm kickr = r(p(0,127),p(4,127));
		Figure kickf = pf(kickr, 36, tr808);
		
		Figure percf = gf(hihatf,drumf,/*hihat_fill1f,hihat_fill2f,clapf,*/kickf,stickf);
		
		Rhythm bassr = r(
				s(0.000,.45,96), s(.5,1.5,99), s(2,20,75),
				s(4,0.45,65), s(4.5,1.5,96), s(5.5,.9,77), s(6.5,.45,73), s(7,.9,69));
		Melody bassm = m(
				an(36), an(38), an(53),
				an(36), an(38), an(50), an(48), an(47));
		Figure bassf = f(bassr, bassm, monosynth);
		
//		Rhythm lead1r = r(
//				s(0.000,4.925,127), s(4.886,1.879,96), s(6.661,1.345,80), s(7.970,5.028,110), s(12.937,1.921,102), s(14.734,1.050,83), s(15.708,2.266,80), s(17.920,3.073,76), s(20.926,0.517,67), s(21.377,0.992,83), s(22.310,1.639,93), s(23.897,0.467,80), s(24.339,1.848,96), s(26.142,1.914,102), s(28.064,0.687,90), s(28.699,0.925,79), s(29.491,1.262,99), s(30.588,0.592,102), s(31.083,0.980,60), s(32.082,3.898,102), s(35.935,9.057,74), s(44.946,0.635,79), s(45.533,0.840,102), s(46.371,0.682,74), s(47.004,1.035,93), s(47.980,0.834,79), s(48.689,0.928,99), s(49.574,3.325,83), s(52.869,0.570,75), s(53.421,1.030,93), s(54.428,0.548,78), s(54.979,0.538,71), s(55.372,0.654,74), s(56.016,6.375,102));
//		Melody lead1m = m(
//			an(74), an(76), an(77), an(65), an(76), an(77), an(60), an(81), an(77), an(76), an(74), an(60), an(62), an(71), an(72), an(74), an(72), an(71), an(60), an(62), an(77), an(76), an(77), an(76), an(77), an(76), an(62), an(77), an(76), an(77), an(76), an(77), an(76), an(64));
//		Figure lead1f = f(lead1r, lead1m, monosynth2);

		// this is pretty awesome
		Rhythm lead1r = r(
				s(0.000,3.051,106), s(3,0.479,106), s(3.5,3.529,102), s(7,0.549,102), s(7.5,3.163,96),
				s(11,0.509,106), s(11.5,1.007,102), s(12.5,1.048,85), s(13.5,0.947,102), s(14.5,1.377,106),
				s(16,3.058,110), s(19,0.445,102), s(19.5,3.592,99), s(23,0.412,102), s(23.5,3.376,76),
				s(27,0.478,74), s(27.5,0.935,110), s(28.5,0.938,93), s(29.5,0.927,110), s(30.5,1.486,110),
				s(32,3.174,110), s(35,0.470,102), s(35.5,3.578,102), s(39,0.468,102), s(39.5,3.303,96),
				s(43,0.514,81), s(43.5,1.147,106), s(44.5,0.988,96), s(45.5,0.939,81), s(46.5,1.538,96),
				s(48,3.064,102), s(51,0.487,106), s(51.5,3.617,99), s(55,0.470,106), s(55.5,4.459,90),
				s(60.5,0.511,99), s(61,0.471,110), s(61.5,0.990,110), s(62.5,1.907,106));
		Melody lead1m = m(
				an(48), an(50), an(52), an(53), an(55),
				an(57), an(55), an(53), an(52), an(53),
				an(48), an(50), an(52), an(53), an(55),
				an(57), an(55), an(57), an(59), an(60),
				an(48), an(50), an(52), an(53), an(55),
				an(57), an(55), an(53), an(52), an(53),
				an(48), an(50), an(52), an(53), an(55),
				an(71), an(72), an(71), an(72));
		Figure lead1f= f(lead1r, lead1m, monosynth2);
		
//		add1(gf(percf,bassf));
//		add1(gf(percf,bassf));
//		add1(gf(percf,bassf,lead1f));
//		add1(gf(percf,bassf));
//		add1(gf(percf,bassf));
//		add1(gf(percf,bassf));
//		add1(gf(percf,bassf));
//		add1(gf(percf,bassf));
//		add1(gf(percf,bassf));
//		add1(gf(percf,bassf));
		
//		add1(gf(percf));
//		add1(gf(percf));
		add1(gf(percf,lead1f));
		add1(gf(percf));
		add1(gf(percf));
		add1(gf(percf));
		add1(gf(percf));
		add1(gf(percf));
		add1(gf(percf));
		add1(gf(percf));
		add1(gf(percf));
		add1(gf(percf));
		
		audition(monosynth2);
	}
	
	@Override
	protected void onCreatePlayer(Player player) {
		CustomInstrumentFactory fac = new CustomInstrumentFactoryImpl(
			0, new CustomInstrumentFactoryImpl.CreateCustomInstrument() {
				@Override
				public RealizedInstrument create(AudioContext ac) {
					DataBead params = BandpassFilterMonoSynthUGen.defaultParams();
					params.put(MonoSynthUGen.ATTACK_TIME_MS, 10);
					params.put(MonoSynthUGen.GLIDE_TIME_MS, 40);
					params.put(BandpassFilterMonoSynthUGen.START_END_FREQ_FACTOR, .5);
					params.put(BandpassFilterMonoSynthUGen.RISE_FREQ_FACTOR, 4);
					params.put(BandpassFilterMonoSynthUGen.CURVATURE, .25);
					BandpassFilterMonoSynthUGen synth = new BandpassFilterMonoSynthUGen(
							ac,
							Buffer.SINE,
							params,
							new double[]{1.0, /*Util.freqShift(7), 2.0, 2.0*Util.freqShift(7), 4.0,*/ 3.0},
							new double[]{1.0, /*0.2, 0.5, 0.1, 0.4,*/ 0.7});
					return new RealizedInstrument(synth, ac);
				}
			},
			1, new CustomInstrumentFactoryImpl.CreateCustomInstrument() {
				@Override
				public RealizedInstrument create(AudioContext ac) {
					DataBead params = MonoSynthUGen.defaultParams();
					params.put(MonoSynthUGen.GLIDE_TIME_MS, 40);
					MonoSynthUGen synth = new MonoSynthUGen(
							ac,
							Buffer.SAW,
							params,
							new double[]{1.0, Util.freqShift(7), 2.0, 2.0*Util.freqShift(7)},
							new double[]{0.6, 0.3, 0.5, 0.2});
					return new RealizedInstrument(synth, ac);
				}
			});
		
		player.setCustomInstrumentFactory(fac);
	}
	
	public static void main(String[] args) throws MidiUnavailableException, IOException {
		Demo6 demo = new Demo6();
		demo.create();
		demo.play();
	}
}
