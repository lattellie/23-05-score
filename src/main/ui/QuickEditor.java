package ui;

import model.Score;
import ui.tools.PianoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuickEditor extends JFrame implements ActionListener, KeyListener {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int[] NOTENUM = {49,81,50,87,51,52,82,53,84,54,89,55};
    private ArrayList<Integer> activatedNotes;
    //initialized to be all null, the last item(get 12, actual 13) is the rest
    private Map<Integer,Integer> keyMap;
    private int upDownPressed = 0;
    // 1 if up is pressed, 0 if nothing pressed, -1 if down is pressed
    private boolean isKeyPressed = false;
    // to make sure there's no double detection
    private boolean isUpDownPressed = false;
    private Score score;
    private JPanel panel;
    private PianoPanel pianoPanel;


    public QuickEditor() {
        super("score editor");
        score = new Score();
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel = new JPanel();
        pianoPanel = new PianoPanel(this);
        add(panel, BorderLayout.SOUTH);
        add(pianoPanel, BorderLayout.CENTER);
        addKeyListener(this);
        setMinimumSize(new Dimension(WIDTH, HEIGHT / 10));
        setVisible(true);
        initializeKeyMap();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuickEditor().setVisible(true));
    }

    public ArrayList<Integer> getActivatedNotes() {
        return activatedNotes;
    }

    public int getUpDownPressed() {
        return upDownPressed;
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

    @Override
    public void actionPerformed(ActionEvent e) {

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (!isUpDownPressed && keyCode == 38 || keyCode == 40) {
            isUpDownPressed = true;
            if (Math.abs(upDownPressed + 39-keyCode) <= 3) {
                upDownPressed += 39-keyCode;
            }
        }
        if (!isKeyPressed && keyMap.get(keyCode) != null) {
            isKeyPressed = true;
            activatedNotes.set(keyMap.get(keyCode),upDownPressed);
            Instant currentTimestamp = Instant.now();
            // Print the current timestamp
            System.out.println("Current timestamp: " + currentTimestamp);
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
        }
        repaint();
    }
}
