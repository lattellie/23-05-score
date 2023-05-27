package ui;

import ui.tools.Metronome;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OuterClass extends JFrame implements ActionListener {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 100;
    private Metronome met;
    private JButton begin;
    private JButton start;
    private JButton stop;
    private JPanel panel;
    private static Thread piano;
    private static Thread metro;
    public OuterClass() {
        super("button window");

        metro = new Thread(new Runnable() {
            @Override
            public void run() {
//                met = Metronome.getMet();
                met = Metronome.getMet();
                System.out.println(met);
                piano.start();
            }
        });

        piano = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(met);
                SwingUtilities.invokeLater(() -> new QuickEditor(met).setVisible(true));
            }
        });

        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        panel = new JPanel();
        initStartStopButtons();
        add(panel, BorderLayout.SOUTH);
        setMinimumSize(new Dimension(WIDTH, HEIGHT / 10));
        setVisible(true);

        metro.start();
    }

    public static Thread getPiano() {
        return piano;
    }

    public static Thread getMetro() {
        return metro;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OuterClass().setVisible(true));
    }
    private void initStartStopButtons() {
        begin = new JButton("begin");
        begin.setActionCommand("begin");
        begin.addActionListener(this);
        start = new JButton("start");
        start.setActionCommand("start");
        start.addActionListener(this);
        stop = new JButton("stop");
        stop.setActionCommand("stop");
        stop.addActionListener(this);
        panel.add(begin);
        panel.add(start);
        panel.add(stop);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand()=="start") {
            met.setRunning(true);
            System.out.println("start");
        } else if (e.getActionCommand() == "stop") {
            System.out.println("stop");
            met.setRunning(false);
        }
    }
}
