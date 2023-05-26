package ui;

import model.EventLog;
import model.Notes;
import model.Score;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.tools.NotesDrawer3;
import ui.tools.PopUpListener;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// the ui of the project
public class ScoreEditor4 extends JFrame implements ActionListener, WindowListener {
    private Score score;
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private static final String[] RIGHT_TONES = new String[]{"C#","Eb","F#","Ab","Bb"};
    private static final String[] TONES = new String[]{"Db","D#","Gb","G#","A#"};
    private static final List<String> TONE_LST = new ArrayList<String>(Arrays.asList(TONES));
    JComboBox<String> dropdown;
    JTextField scoreText;
    JButton editScore;
    private JPanel panel;
    private JPanel scorePropertyPanel;
    private NotesDrawer3 notesPanel;
    private JTextField textField;
    private JLabel label;
    private JButton noteBtn;
    private JButton svBtn;
    private JButton ldBtn;
    private JButton delBtn;
    private JButton upBtn;
    private JButton downBtn;
    private JButton imgBtn;
    private JButton popupBtn;
    private JButton playBtn;
    private static final String JSON_STORE = "./data/score.json";
    private JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
    private JsonReader jsonReader = new JsonReader(JSON_STORE);

    // constructor for this class
    public ScoreEditor4() {
        super("score editor");
        addWindowListener(this);
        score = new Score();
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel = new JPanel();
        scorePropertyPanel = new JPanel();
        setMinimumSize(new Dimension(WIDTH, HEIGHT / 10));
        initializeToolBar();
        initializeNotesDrawer();
        initializeSaveLoad();
        initializeUpDown();
        initializeScoreProperty();
        initializePopUp();
        setVisible(true);
    }

    private void initializeMidiSynth() throws MidiUnavailableException, InterruptedException {
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();

        MidiChannel[] channels = synth.getChannels();
        channels[0].programChange(1);
        for (int i = 36; i < 40; i++) {
            channels[0].noteOn(i, 0);
//            channels[1].noteOn(i + 1,500);
            Thread.sleep(500);
            channels[0].noteOff(i);
        }
        for (Notes n : score.getNotesArray()) {
            int noteHeight = n.getRelKey() + 36 + n.getOctave() * 12;
            int delay = (int) (4 * 60000 / score.getTempo() * n.getBeat());
            System.out.println(delay);
            if (n.getRelKey() != 13) {
                channels[0].noteOn(noteHeight, 100);
                Thread.sleep(delay - 10);
                channels[0].noteOff(noteHeight);
                Thread.sleep(10);
            } else {
                Thread.sleep(delay);
            }

        }

        synth.close();
    }

    // EFFECTS: create a popup window
    private void initializePopUp() {
        popupBtn = new JButton("quick editor");
        popupBtn.setActionCommand("pop");
        panel.add(popupBtn);
        popupBtn.addActionListener(new PopUpListener());
        playBtn = new JButton("play!");
        playBtn.setActionCommand("play");
        panel.add(playBtn);
        playBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("play")) {
                    try {
                        initializeMidiSynth();
                    } catch (MidiUnavailableException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    midiOutNotes();
                }
            }
        });
    }

    private void midiOutNotes() {

    }

    // MODIFIES: this
    // EFFECTS: add the buttons that edit scoreproperty
    private void initializeScoreProperty() {
        String[] items = {"Edit Clef", "Edit Title", "Edit Tempo", "Edit TS", "Edit Tone", "delete", "add", "switch"};
        // Create dropdown menu with list of items
        scorePropertyPanel.setLayout(new GridLayout(2, 2));
        dropdown = new JComboBox<>(items);
        scoreText = new JTextField(10);
        editScore = new JButton("EDIT!");
        imgBtn = new JButton("print jpg");
        scorePropertyPanel.add(scoreText);
        scorePropertyPanel.add(dropdown);
        scorePropertyPanel.add(editScore);
        scorePropertyPanel.add(imgBtn);
        dropdown.setActionCommand("dropdown");
        editScore.setActionCommand("edit");
        imgBtn.setActionCommand("SvImg");
        dropdown.addActionListener(this);
        editScore.addActionListener(this);
        imgBtn.addActionListener(this);
        panel.add(scorePropertyPanel, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: add button that scale up / down the score
    private void initializeUpDown() {
        upBtn = new JButton("up");
        downBtn = new JButton("down");
        upBtn.setActionCommand("up");
        downBtn.setActionCommand("down");
        panel.add(upBtn);
        panel.add(downBtn);
        upBtn.addActionListener(this);
        downBtn.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: add buttons that save / load content
    private void initializeSaveLoad() {
        svBtn = new JButton("save score");
        ldBtn = new JButton("load score");
        svBtn.setActionCommand("saveScore");
        ldBtn.setActionCommand("loadScore");
        panel.add(svBtn);
        panel.add(ldBtn);
        svBtn.addActionListener(this);
        ldBtn.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: create a new panel for notes editing
    private void initializeNotesDrawer() {
        notesPanel = new NotesDrawer3(score, null);
        add(notesPanel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: create a new panel for tool bar
    private void initializeToolBar() {
        initializeNoteAdder();
        add(panel, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: create the button, label and textField for adding notes
    private void initializeNoteAdder() {
        label = new JLabel("Notes Format: A%10400");
        label.setSize(new Dimension(200, 20));
        textField = new JTextField(8);
        noteBtn = new JButton(("Add Note"));
        delBtn = new JButton("Delete Note");
        noteBtn.setActionCommand("addNote");
        delBtn.setActionCommand("deleteNote");
        noteBtn.addActionListener(this);
        delBtn.addActionListener(this);
        panel.add(label);
        panel.add(textField);
        panel.add(noteBtn);
        panel.add(delBtn);
    }

    // EFFECTS: specify the action required responding to the user's action
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("addNote")) {
            String n = textField.getText();
            if (n.length() == 7) {
                score.addNotes(new Notes(n));
                label.setText("added notes: " + n);
                textField.setText("");
                repaint();
            }
        }
        if (e.getActionCommand().equals("deleteNote")) {
            ArrayList<Notes> n = score.getNotesArray();
            n.remove(n.size() - 1);
            repaint();
        }
        saveLoadListener(e);
        upDownListener(e);
        editScoreListener(e);
        if (e.getActionCommand().equals("SvImg")) {
            notesPanel.toImage();
        }
    }


    // MODIFIES: score
    // EFFECTS: modifies the score's property based on the user's action
    private void editScoreListener(ActionEvent e) {
        if (e.getActionCommand().equals("edit")) {
            String s = (String) dropdown.getSelectedItem();
            String n = scoreText.getText();
            assert s != null;
            if (s.equals("Edit Clef")) {
                score.setClef(n.toCharArray()[0]);
            } else if (s.equals("Edit Title")) {
                score.setName(n);
            } else if (s.equals("Edit Tempo")) {
                score.setTempo(Integer.parseInt(n));
            } else if (s.equals("Edit TS")) {
                score.setTimeSignature(n);
            } else if (s.equals("Edit Tone")) {
                score.setTone(getTone(n));
            } else if (s.equals("delete")) {
                score.getNotesArray().remove(Integer.parseInt(n) - 1);
            } else if (s.equals("add")) {
                score.getNotesArray().add(Integer.parseInt(n) - 1, new Notes(textField.getText()));
            } else if (s.equals("switch")) {
                switchNotes(textField.getText(), Integer.parseInt(n) - 1);
            }
            repaint();
        }
    }

    // EFFECTS: return the proper tone
    private String getTone(String n) {
        if (TONE_LST.contains(n)) {
            return RIGHT_TONES[TONE_LST.indexOf(n)];
        } else {
            return n;
        }
    }

    // Switch the i-th note in the score's tone to the new one
    private void switchNotes(String s, int i) {
        Notes n = score.getNotesArray().get(i);
        score.getNotesArray().remove(i);
        try {
            score.getNotesArray().add(i, new Notes(s.substring(0,2),n.getBeat(),Integer.parseInt(s.substring(5,7))));
        } catch (Exception e) {
            score.getNotesArray().add(i, new Notes(s.substring(0,2), n.getBeat(),n.getOctave()));
        }
    }

    // MODIFIES: score
    // EFFECTS: scale all the notes in the score up/down
    private void upDownListener(ActionEvent e) {
        if (e.getActionCommand().equals("up")) {
            score.reTune(1);
            repaint();
        }
        if (e.getActionCommand().equals("down")) {
            score.reTune(-1);
            repaint();
        }
    }

    // EFFECTS: save / load the saved file
    private void saveLoadListener(ActionEvent e) {
        if (e.getActionCommand().equals("saveScore")) {
            saveScore();
            repaint();
        }
        if (e.getActionCommand().equals("loadScore")) {
            loadScore();
            repaint();
        }
    }

    // MODIFIES: this
    // EFFECTS: load score from file
    private void loadScore() {
        try {
            Score s = jsonReader.read();
            ArrayList<Notes> nt = jsonReader.read().getNotesArray();
            for (Notes n : nt) {
                score.addNotes(n);
            }
            score.setClef(s.getClef());
            score.setTempo(s.getTempo());
            score.setTimeSignature(s.getTimeSignature());
            score.setName(s.getName());
            score.setTone(s.getTone());
//          label.setText("file loaded");
        } catch (IOException e) {
            label.setText("unable to load file");
        }
    }

    // EFFECTS: saves the score to file
    private void saveScore() {
        try {
            jsonWriter.open();
            jsonWriter.write(score);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            label.setText("unable to save file");
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    // EFFECTS: log the event once the window is closed
    @Override
    public void windowClosing(WindowEvent e) {
        for (model.Event event: EventLog.getInstance()) {
            System.out.println(event.getDescription() + " at " + event.getDate());
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }


    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScoreEditor4().setVisible(true));
    }

}
