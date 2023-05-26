//package ui;
//
//import ui.tools.PianoPanel;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//
//public class QuickEditorTry implements KeyListener, ActionListener {
//    private JFrame frame;
//    private JPanel toolPanel;
//    private PianoPanel piano;
//
//    public QuickEditorTry() {
//        frame = new JFrame("piano");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        piano = new PianoPanel();
//        piano.setPreferredSize(new Dimension(400, 400));
//        frame.getContentPane().add(piano);
//        frame.addKeyListener(this);
//        frame.pack();
//        frame.setVisible(true);
//    }
//
//    @Override
//    public void keyTyped(KeyEvent e) {
//
//    }
//
//    @Override
//    public void keyPressed(KeyEvent e) {
//        piano.repaint();
//    }
//
//    @Override
//    public void keyReleased(KeyEvent e) {
//
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//
//    }
//}
