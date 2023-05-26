package ui.tools;

import javax.swing.*;
import java.awt.*;

public class PianoPanel extends JPanel {

    private static final Color BG = new Color(160,180,180);
    private int w;
    private int h;
    private int keytop = 50;
    private int keyheight = 60;
    private int keywidth = 20;
    public PianoPanel(){
        super();
        setBackground(BG);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        w = this.getWidth();
        h = this.getHeight();
        keyheight = h / 2;
        keywidth = w / 14;
        g.setColor(Color.white);
        int left = (int) (w/2-keywidth*3.5);
        for (int i=0; i<7; i++) {
            g.fillRect(left + i * keywidth, keytop, (int) (keywidth * 0.9), keyheight);
        }
        g.setColor(Color.black);
        double ratio = 0.7;
        for (int n : new int[]{1,2,4,5,6}) {
            g.fillRect((int) (left - keywidth * ratio / 2 + n * keywidth), keytop, (int) (keywidth * ratio), keyheight*3/5);
        }
    }
}
