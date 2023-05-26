package ui;

import model.Score;
import ui.tools.NotesDrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScoreEditor extends JFrame implements ActionListener {

    private Score score;
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private JPanel panel;
    private JPanel scorePropertyPanel;
    private NotesDrawer notesPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScoreEditor().setVisible(true));
    }

    public ScoreEditor() {
        super("score editor");
        score = new Score();
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel = new JPanel();
        scorePropertyPanel = new JPanel();
        notesPanel = new NotesDrawer();
        initializeToolBar();
    }

    // set up the tool bars
    private void initializeToolBar() {

    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
