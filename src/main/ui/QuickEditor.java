package ui;

import model.Notes;
import model.Score;
import ui.tools.Metronome;
import ui.tools.PianoPanel;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// TODO: set up rest
// check if branch's made
public class QuickEditor extends JFrame implements KeyListener {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int[] NOTENUM = {49,81,50,87,51,52,82,53,84,54,89,55,0,32};
    // 0 is a stub
    private ArrayList<Integer> activatedNotes;
    //initialized to be all null, the last item(get 12, actual 13) is the rest
    private Map<Integer,Integer> keyMap;
    private int curOctave = 0;
    // 1 if up is pressed, 0 if nothing pressed, -1 if down is pressed
    private boolean isKeyPressed = false;
    // to make sure there's no double detection
    private boolean isUpDownPressed = false;
    private static Score score;
//    private JPanel panel;
    private static PianoPanel pianoPanel;
    protected static boolean startPrinting = false;
    private Synthesizer synth;
    private MidiChannel[] channels;
//    private static Map<TempNote,Instant> noteMapList;
    private static ArrayList<TempNote> noteList;
    private static ArrayList<Instant> timeList;
    private static boolean isRunning = false;

//    static Thread piano;
//    static Thread metro;
    private Metronome met;

    protected JLabel label = new JLabel();

    public QuickEditor(Metronome met) {
        super("score editor");
        this.met = met;
//        noteMapList = new HashMap<>();
        noteList = new ArrayList<>();
        timeList = new ArrayList<>();
        basicConstructor();

    }

    private void basicConstructor() {
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
//        panel = new JPanel();
        pianoPanel = new PianoPanel(this);
        pianoPanel.add(label);
//        add(panel, BorderLayout.SOUTH);
        add(pianoPanel, BorderLayout.CENTER);
        addKeyListener(this);
        setMinimumSize(new Dimension(WIDTH, HEIGHT / 10));
        setVisible(true);
        initializeKeyMap();
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            channels = synth.getChannels();
            channels[0].programChange(1);
        } catch (MidiUnavailableException e) {
            System.out.println("midi synth unavailable exception");
        }
    }

//    public void displayMessage(String str) {
//        label.setText(str);
//        repaint();
//    }

    public ArrayList<Integer> getActivatedNotes() {
        return activatedNotes;
    }

    public int getCurOctave() {
        return curOctave;
    }

    private void initializeKeyMap() {
        keyMap = new HashMap<>();
        activatedNotes = new ArrayList<>();
        for (int i = 0; i < NOTENUM.length;i++) {
            keyMap.put(NOTENUM[i],i);
            activatedNotes.add(null);
        }
        activatedNotes.add(null);
    }
//
//    public static Map<TempNote, Instant> getNoteMapList() {
//        return noteMapList;
//    }

    public static ArrayList<TempNote> getNoteList() {
        return noteList;
    }

    public static ArrayList<Instant> getTimeList() {
        return timeList;
    }

    protected static class TempNote {
        protected int keyNum;
        protected int octave;
        public TempNote(int keyNum, int octave) {
            if (keyNum == 12) {
                this.keyNum = 0;
            } else {
                this.keyNum = keyNum;
            }
            this.octave = octave;
        }

        public Notes getNotes(float beat) {
            return new Notes(keyNum,beat,octave);
        }
    }
    public void produceNoteSound(TempNote tn) {
        try {
            int key = 60 + tn.octave * 12 + tn.keyNum;
            if (tn.keyNum == 13) {
                channels[9].noteOn(77,50);
            } else {
                channels[0].noteOn(key,50);
            }
        } catch (Exception e) {
            System.out.println("interrupted exception");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
//        if (keyCode == 83) {
//            met.setRunning(false);
//        }
        if (!isUpDownPressed && keyCode == 38 || keyCode == 40) {
            isUpDownPressed = true;
            if (Math.abs(curOctave + 39-keyCode) <= 3) {
                curOctave += 39-keyCode;
            }
        }
        if (!isKeyPressed && keyMap.get(keyCode) != null) {
            isKeyPressed = true;
            activatedNotes.set(keyMap.get(keyCode), curOctave);
            Instant currentTimestamp = Instant.now();
            TempNote tn = new TempNote(keyMap.get(keyCode), curOctave);
//            noteMapList.put(tn, currentTimestamp);
            noteList.add(tn);
            timeList.add(currentTimestamp);
//            System.out.println("Current timestamp: " + currentTimestamp);
            produceNoteSound(tn);
//            System.out.println(notesList);
        }
        repaint();

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == 38 || keyCode == 40) {
            isUpDownPressed = false;
        } else if (keyMap.get(keyCode) != null) {
            isKeyPressed = false;
            activatedNotes.set(keyMap.get(keyCode),null);
        } else if (keyCode == KeyEvent.VK_ENTER) {
            startPrinting = true;
            System.out.println("entered!");
        }
        repaint();
    }

    public static Score getEditedScore(int tempo) {
        score = new Score();
        double minSplit = (double) (60.0/tempo);
        for (int i=0; i<noteList.size()-1; i++) {
            Duration d = Duration.between(timeList.get(i),timeList.get(i+1));
            double sec = d.toMillis()*0.001;
            int beat = (int) Math.round(sec/minSplit);
            score.addNotes(noteList.get(i).getNotes(0.0625f*beat));
        }
        return score;
    }

}