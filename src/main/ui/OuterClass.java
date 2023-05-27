package ui;

import ui.tools.Metronome;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("ALL")
public class OuterClass extends JFrame implements ActionListener {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 100;
    private Metronome met;
    private JButton finish;
    private JButton start;
    private JButton stop;
    private JPanel panel;
    private JTextField tempoInsert;
    private static Thread piano;
    private static Thread metro;
    public OuterClass() {
        super("button window");

        metro = new Thread(new Runnable() {
            @Override
            public void run() {
//                met = Metronome.getMet();
                met = Metronome.getMet();
                int tem = Integer.parseInt(tempoInsert.getText());
                if (tem > 20) {
                    Metronome.setTempo(tem);
                }
//                if (tem > 20) {
//                    met = Metronome.getMet(tem);
//                } else {
//                    met = Metronome.getMet();
//                }
                System.out.println(met);
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
        add(panel, BorderLayout.CENTER);
        setMinimumSize(new Dimension(WIDTH, HEIGHT / 10));
        setVisible(true);

        piano.start();
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
        finish = new JButton("finish");
        finish.setActionCommand("finish");
        finish.addActionListener(this);
        start = new JButton("start");
        start.setActionCommand("start");
        start.addActionListener(this);
        stop = new JButton("stop");
        stop.setActionCommand("stop");
        stop.addActionListener(this);
        tempoInsert = new JTextField();
        tempoInsert.setColumns(20);
        panel.add(tempoInsert);
        panel.add(finish);
        panel.add(start);
        panel.add(stop);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand()=="start") {
            int tem;
            try {
                tem = Integer.parseInt(tempoInsert.getText());
            } catch (NumberFormatException er) {
                tem = 120;
            }
            try {
                Metronome.setTempo(tem);
                metro.start();
            } catch (IllegalThreadStateException err) {
                Metronome.setTempo(tem);
                metro.resume();
            }

            System.out.println("start");
        } else if (e.getActionCommand() == "stop") {
            System.out.println("stop");
            metro.suspend();
        } else if (e.getActionCommand() == "finish") {
            metro.stop();
        }
    }
}
