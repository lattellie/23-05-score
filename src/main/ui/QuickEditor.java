package ui;

import model.Score;
import ui.tools.PianoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class QuickEditor extends JFrame implements ActionListener, KeyListener {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

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
        pianoPanel = new PianoPanel();
        add(panel, BorderLayout.SOUTH);
        add(pianoPanel, BorderLayout.CENTER);
        setMinimumSize(new Dimension(WIDTH, HEIGHT / 10));
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuickEditor().setVisible(true));
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
