package ui.tools;

import ui.OuterClass;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class Metronome {
    private static boolean isRunning;
    private static Metronome met;
    private static Synthesizer synth;
    private static MidiChannel[] channels;
    private static int tempo;
    private Metronome() {
        tempo = 120;
        basicConstructor(true);
    }

    public static Metronome getMet() {
        if (met == null) {
            met = new Metronome();
        }
        return met;
    }
    public static Metronome getMet(int tem) {
        if (met == null) {
            met = new Metronome(true, tem);
        } else {
            tempo = tem;
        }
        return met;
    }

    public static void setTempo(int tem) {
        tempo = tem;
    }
    private Metronome(Boolean isRunning, int t) {
        tempo = t;
        basicConstructor(isRunning);
    }
    public static void basicConstructor(Boolean isRunning) {
        try {
            Metronome.isRunning = isRunning;
            synth = MidiSystem.getSynthesizer();
            synth.open();
            channels = synth.getChannels();
            channels[9].programChange(0,0);
//            startMetro();
            while (isRunning) {
                channels[9].noteOn(36, 100);
                OuterClass.getMetro().sleep((long) 60000/tempo);
                channels[9].noteOff(36);
            }
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void startMetro() throws InterruptedException{
        while (isRunning) {
            channels[9].noteOn(36, 100);
            OuterClass.getMetro().sleep((long) 60000/tempo);
            channels[9].noteOff(36);
        }
    }
    public void setRunning(boolean b) {
        isRunning = b;
        basicConstructor(b);
    }
    public void stopRunning() {
        isRunning = false;
    }

}
