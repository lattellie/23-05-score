package ui.tools;

import ui.QuickEditor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PianoPanel extends JPanel {

    private static final Color BG = new Color(160,180,180);
    private int w;
    private int h;
    private int keytop = 50;
    private int keyheight = 60;
    private int keywidth = 20;

    private QuickEditor frame;
    public PianoPanel(QuickEditor frame){
        super();
        setBackground(BG);
        this.frame = frame;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ArrayList<Integer> activatedNotes = frame.getActivatedNotes();
        int octave = frame.getUpDownPressed();
        w = this.getWidth();
        h = this.getHeight();
        keyheight = h / 2;
        keywidth = w / 14;
        g.setColor(Color.white);
        int left = (int) (w/2-keywidth*3.5);
        int sqwidth = keyheight / 7;
        for (int i=0; i<7; i++) {
            if (i-3 == octave) {
                g.setColor(Color.pink);
            }
            g.fillRect(left-sqwidth, keytop+sqwidth/5+(6-i)*sqwidth, sqwidth*4/5, sqwidth*4/5);
            g.setColor(Color.white);
        }
        int[] whiteNode = new int[]{0,2,4,5,7,9,11};
        int[] blackNode = new int[]{1,3,6,8,10};
        int[] blackRect = new int[]{1,2,4,5,6};
        for (int i=0; i<7; i++) {
            if (activatedNotes.get(whiteNode[i]) != null) {
                g.setColor(Color.PINK);
            }
            g.fillRect(left + i * keywidth, keytop, (int) (keywidth * 0.9), keyheight);
            g.setColor(Color.white);
        }
        g.setColor(Color.black);
        double ratio = 0.7;
        for (int i=0; i<5; i++) {
            if (activatedNotes.get(blackNode[i]) != null) {
                g.setColor(Color.red);
            }
            g.fillRect((int) (left - keywidth * ratio / 2 + blackRect[i] * keywidth), keytop, (int) (keywidth * ratio), keyheight*3/5);
            g.setColor(Color.black);
        }
    }
}
