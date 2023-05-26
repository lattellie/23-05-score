package ui.tools;

import model.Notes;
import model.Score;
import ui.exceptions.RenderedException2;
import ui.exceptions.RestException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// the class dealing with the drawing panel of score
public class NotesDrawer3 extends JPanel {
    private static final Color BG = new Color(220, 227, 227);
    private static final Color NT = new Color(50, 50, 50);
    private static final int SMALL = 12;
    private static final int LARGE = SMALL * 7;
    private static final int MAX_BEAT = 4;
    private static final int TOP_HEIGHT = SMALL * 4;
    private static final int NOTE_WIDTH = SMALL;
    private static final int NOTE_HEIGHT = (int) (SMALL * 0.8);
    private static final int ROW_H = LARGE + 4 * SMALL;
    private static final double LEFT_PERCENTAGE = 0.1;
    private static final int TOP_CENTER = LARGE + (int) (SMALL * 4.6);
    private int topCenterC = LARGE + (int) (SMALL * 4.6);
    private Map<String, int[]> major = new HashMap<>();
    private Map<String, int[]> noteMap = new HashMap<>();
    private Map<String, int[]> toneMap = new HashMap<>();
    private Score score;
    private Graphics gra;

    public NotesDrawer3(Score score, Class noteProperty) {
        super();
        setBackground(BG);
        setMajor();
        setNoteMap();
        setToneMap();
        this.score = score;
        this.gra = null;
    }

    // MODIFIES: toneMAp
    // EFFECTS: set up the tone map, first element 1: sharp, -1: flat.
    private void setToneMap() {
        toneMap.put("C", new int[]{0, 0});
        toneMap.put("C#", new int[]{1, 7});
        toneMap.put("D", new int[]{1, 2});
        toneMap.put("Eb", new int[]{-1, 3});
        toneMap.put("E", new int[]{1, 4});
        toneMap.put("F", new int[]{-1, 1});
        toneMap.put("F#", new int[]{1, 6});
        toneMap.put("G", new int[]{1, 1});
        toneMap.put("Ab", new int[]{-1, 4});
        toneMap.put("A", new int[]{1, 3});
        toneMap.put("Bb", new int[]{-1, 2});
        toneMap.put("B", new int[]{1, 5});
    }

    // MODIFIES: noteMap
    // EFFECTS: set up the note map
    private void setNoteMap() {
        noteMap.put("C", new int[]{1, 1, 2, 3, 3, 4, 4, 5, 5, 6, 7, 7});
        noteMap.put("C#", new int[]{0, 1, 2, 2, 3, 3, 4, 5, 5, 6, 6, 7});
        noteMap.put("D", new int[]{1, 1, 2, 3, 3, 4, 4, 5, 5, 6, 7, 7});
        noteMap.put("Eb", new int[]{1, 2, 2, 3, 3, 4, 5, 5, 6, 6, 7, 7});
        noteMap.put("E", new int[]{1, 1, 2, 3, 3, 4, 4, 5, 5, 6, 7, 7});
        noteMap.put("F", new int[]{1, 1, 2, 3, 3, 4, 4, 5, 5, 6, 7, 7});
        noteMap.put("F#", new int[]{1, 1, 2, 2, 3, 3, 4, 5, 5, 6, 6, 7});
        noteMap.put("G", new int[]{1, 1, 2, 3, 3, 4, 4, 5, 6, 6, 7, 7});
        noteMap.put("Ab", new int[]{1, 2, 2, 3, 3, 4, 4, 5, 6, 6, 7, 7});
        noteMap.put("A", new int[]{1, 1, 2, 3, 3, 4, 4, 5, 5, 6, 7, 7});
        noteMap.put("Bb", new int[]{1, 2, 2, 3, 3, 4, 4, 5, 6, 6, 7, 7});
        noteMap.put("B", new int[]{1, 1, 2, 2, 3, 4, 4, 5, 5, 6, 6, 7});
    }

    // MODIFIES: mapMajor
    // EFFECTS: set up the mapMajor
    private void setMajor() {
        major.put("C", new int[]{0, 1, 0, -1, 0, 0, 1, 0, 1, 0, -1, 0});
        major.put("C#", new int[]{0, 0, 2, 0, 2, 0, 0, 2, 0, 2, 0, 2});
        major.put("D", new int[]{2, 0, 0, -1, 0, 2, 0, 0, 1, 0, -1, 0});

        major.put("Eb", new int[]{0, -1, 0, 0, 2, 0, 1, 0, 0, 2, 0, 2});
        major.put("E", new int[]{2, 0, 2, 0, 0, 2, 0, 2, 0, 0, -1, 0});
        major.put("F", new int[]{0, 1, 0, -1, 0, 0, 1, 0, 1, 0, 0, 0, 2});

        major.put("F#", new int[]{2, 0, 2, 0, 2, 0, 0, 2, 0, 2, 0, 0});
        major.put("G", new int[]{0, 1, 0, -1, 0, 2, 0, 0, 1, 0, -1, 0});
        major.put("Ab", new int[]{0, 0, 2, 0, 2, 0, 1, 0, 0, 2, 0, 2});

        major.put("A", new int[]{2, 0, 0, -1, 0, 2, 0, 2, 0, 0, -1, 0});
        major.put("Bb", new int[]{0, 1, 0, 0, 2, 0, 1, 0, 1, 0, 0, 2});
        major.put("B", new int[]{2, 0, 2, 0, 0, 2, 0, 2, 0, 2, 0, 0});
    }


    // EFFECTS: save the image
    public void toImage() {
        int w = gra.getClipBounds().width;
        int h = gra.getClipBounds().height;
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        // Get the graphics object for the image
        Graphics g2d = image.createGraphics();
//        System.out.println(g2d);
        paintComponent(g2d);
//        System.out.println(g2d);
        try {
            File output = new File("output.jpg");
            ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            System.out.println("Error saving image: " + e.getMessage());
        }
        g2d.dispose();
    }

    // MODIFIES: g
    // EFFECTS: render the whole jpanel area
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        double beatTotal = 0;
        List<NoteProperty> npLst = new ArrayList<>();
        for (Notes n : score.getNotesArray()) {
            try {
                int[] xyUp = renderNotes(n, g, beatTotal);
                if (n.getBeat() < 1) {
                    smaller(g, beatTotal, npLst, n, xyUp);
                }
            } catch (RestException e) {
                renderRest(e.getBeat(), e.getWidth(), e.getHeight(), e.getGraphic(), npLst);
            } catch (RenderedException2 e) {
                npLst.add(e.getNoteProperty());
                drawVerticalLines(npLst, e.getBooleanUp(), g);
                npLst.clear();
            }
            beatTotal += n.getBeat();
        }
        this.gra = g;
    }

    // MODIFIES: g
    // EFFECTS: deal with beat that's smaller than 1
    private void smaller(Graphics g, double bt, List<NoteProperty> lst, Notes n, int[] xyUp) throws RenderedException2 {
        if (n.getBeat() < 0.25) {
            lst.add(recordDrawLine(xyUp, n.getBeat(), bt + n.getBeat()));
        } else {
            drawVerticalLine(xyUp, SMALL * 4, g);
        }
    }

    // MODIFIES: g
    // EFFECT: draw the vertical line of the notes
    private void drawVerticalLines(List<NoteProperty> npLst, boolean up, Graphics g) {
        int h = 0;
        int q = 0;
        ArrayList<Integer> b = new ArrayList<>();
        if (up) {
            h = getHeight();
            for (NoteProperty np : npLst) {
                h = Math.min(h, np.height) + 5 * SMALL;
            }
        } else {
            for (NoteProperty np : npLst) {
                h = Math.max(h, np.height) - 5 * SMALL;
                q = NOTE_WIDTH;
            }
        }
        for (NoteProperty np : npLst) {
            if (np.beat != -1) {
                g.drawLine(np.width + q, np.height, np.width + q, h);
            }
            for (int n = 0; n < Math.abs(np.beat); n++) {
                b.add(np.beat);
            }
        }
        drawHorizontalLine(npLst, g, h, q, b);
    }

    // MODIFIES: g
    // EFFECTS: draw the horizontal line that binds notes together
    private void drawHorizontalLine(List<NoteProperty> npLst, Graphics g, int h, int q, ArrayList<Integer> b) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(1.5F));
        int left = npLst.get(0).width + q;
        int right = npLst.get(npLst.size() - 1).width + q;
        int width = (right - left) / (npLst.size() - 1);
        int height = h + q / 2;
        height = dealWithRest(g, h, q, b, left, right, width, height);
        if (b.get(0).equals(b.get(1)) && b.get(2).equals(b.get(3))) {
            if (b.get(0) == 2 && b.get(2) == 1) {
                g.drawLine(right - (right - left) / 3, height, right, height);
            } else if (b.get(3) == 2 && b.get(0) == 1) {
                g.drawLine(left, height, left + width, height);
            } else if (b.get(0) == 1 && b.get(2) == 1) {
                g.drawLine(left, height, right, height);
            }
        } else {
            if (b.get(0).equals(1) && b.get(3).equals(1)) {
                g.drawLine(left + width / 2, height, left, height);
                g.drawLine(right, height, right - width / 2, height);
            }
        }
        g2d.setStroke(new BasicStroke(1));
    }

    // MODIFIES: g
    // EFFECTS: deal with the cases where there's rest with beat < 0.25
    private int dealWithRest(Graphics g, int h, int q, ArrayList<Integer> b, int l, int right, int width, int height) {
        int length;
        if (q == 0) {
            height = height - NOTE_WIDTH / 2;
            length = - NOTE_WIDTH * 3;
        } else {
            length = NOTE_WIDTH * 3;
        }
        if (b.get(0).equals(-1)) {
            if (b.get(1).equals(-1)) {
                int half = NOTE_WIDTH / 2;
                if (b.get(2).equals(b.get(3))) {
                    g.drawLine(right, h, right + half, h + length);
                } else if (b.get(2).equals(-1)) {
                    g.drawLine(right, h, right + half, h + length);
                    int h2 = h + length / 6;
                    g.drawLine(right, h2, right + half, h2 + length);
                } else {
                    g.drawLine(right - width, h, right, h);
                }
            } else {
                g.drawLine(l + width, h, right, h);
                g.drawLine(l + width, height, right, height);
            }
        } else {
            g.drawLine(l, h, right, h);
        }
        return height;
    }

    // the class for recording the notes' x, y and the beat * 4
    public class NoteProperty {
        private int width;
        private int height;
        private int beat;

        public NoteProperty(int x, int y, int b) {
            this.width = x;
            this.height = y;
            this.beat = b;
        }
    }

    // EFFECTS: return the NoteProperty (x, y, beat) that need to be drawn
    public NoteProperty recordDrawLine(int[] xyUp, float beat, double oneBeat) throws RenderedException2 {
        NoteProperty np = new NoteProperty(xyUp[0], xyUp[1], (int) (beat * 16));
        if ((oneBeat * 4) % 1 == 0) {
            throw new RenderedException2(xyUp[2], np);
        } else {
            return np;
        }
    }

    // MODIFIES: g
    // EFFECT: draw rests onto the graphics
    private void renderRest(float beat, int width, int height, Graphics g, List<NoteProperty> npLst) {
        int w = NOTE_WIDTH;
        if (beat == 1) {
            g.fillRect(width + w / 2, height, w, SMALL / 2);
            g.drawLine(width, height, (int) (width + 2 * w), height);
        } else if (beat == 0.5) {
            g.fillRect(width + w / 2, height + SMALL / 2, w, SMALL / 2);
            g.drawLine(width, height + SMALL, (int) (width + 2 * w), height + SMALL);
        } else if (beat == 0.25) {
            drawSmallRest(width, height, g, 4);
        } else if (beat == 0.125) {
            npLst.add(new NoteProperty(width, height, -1));
            npLst.add(new NoteProperty(width, height, -1));
            drawSmallRest(width, height, g, 8);
        } else if (beat == 0.0625) {
            npLst.add(new NoteProperty(width, height, -1));
            drawSmallRest(width, height, g, 16);
        }
    }

    // MODIFIES g
    // EFFECT: draw the rest onto the graphic
    private void drawSmallRest(int width, int height, Graphics g, int beat) {
        BufferedImage image;
        File f;
        int addH = -SMALL / 2;
        int s = (int) (SMALL * 2.5);
        int add = s / 2;
        if (beat == 4) {
            f = new File("src/main/ui/imgs/fourth.png");
            addH = 0;
        } else if (beat == 8) {
            f = new File("src/main/ui/imgs/eighth.png");
            add = 0;
        } else {
            f = new File("src/main/ui/imgs/sixteenth.png");
            add = -s / 2;
        }
        try {
            image = ImageIO.read(f);
            g.drawImage((Image) image, width + add, height + addH, s, s, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // MODIFIES: g
    // EFFECTS: draw the vertical line for notes with length <= 0.5
    private void drawVerticalLine(int[] xyUp, int h, Graphics g) {
        if (xyUp[2] == 1) {
            g.drawLine(xyUp[0], xyUp[1], xyUp[0], xyUp[1] + h * xyUp[2]);
        } else {
            g.drawLine(xyUp[0] + NOTE_WIDTH, xyUp[1], xyUp[0] + NOTE_WIDTH, xyUp[1] + h * xyUp[2]);
        }
    }

    // MODIFIES: g
    // EFFECTS: render notes on the panel, return the x, y and direction of the vertical line
    private int[] renderNotes(Notes n, Graphics g, double curBeat) throws RestException {
        int a = (int) ((double) score.getTimeSignatureIntList()[0] / score.getTimeSignatureIntList()[1] * MAX_BEAT);
        double b = curBeat % a;
        double bb = (b / a * MAX_BEAT);
        double cur = ((((double) ((int) bb)) / MAX_BEAT) + (0.05 + ((bb % 1) * 0.9)) / MAX_BEAT) * getWidth();
        double w = cur * (1 - LEFT_PERCENTAGE) + getWidth() * LEFT_PERCENTAGE;
        Color sv = g.getColor();
        String maj = score.getTone();
        int topLine = (int) curBeat / a * ROW_H + TOP_HEIGHT;
        double addHeight = calculateHeight(maj, n.getOctave(), n.getRelKey());
        int noteHeight = (int) (topLine + TOP_CENTER - addHeight * SMALL);
        drawNotes(n, g, w, topLine, addHeight, noteHeight);
        drawAccent(maj, w, noteHeight, n.getRelKey(), g);
        if (addHeight >= 3) {
            return new int[]{(int) w, noteHeight + NOTE_HEIGHT / 2, 1};
        } else {
            return new int[]{(int) w, noteHeight + NOTE_HEIGHT / 2, -1};
        }
    }

    // MODIFIES: g
    // EFFECTS: draw the accent next to the notes that's sharp/ flat
    private void drawAccent(String maj, double w, int noteHeight, int relKey, Graphics g) {
        int a;
        int[] b = major.get(maj);
        a = b[relKey];
        try {
            BufferedImage image = null;
            if (a == -1) {
                image = ImageIO.read(new File("src/main/ui/imgs/flat.png"));
            } else if (a == 1) {
                image = ImageIO.read(new File("src/main/ui/imgs/sharp.png"));
            } else if (a == 2) {
                image = ImageIO.read(new File("src/main/ui/imgs/natural.png"));
            }
            int n = NOTE_WIDTH * 5 / 4;
            g.drawImage(image, (int) (w - NOTE_WIDTH), noteHeight - NOTE_HEIGHT / 2, n, n, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // MODIFIES: g
    // EFFECT: draw notes top of graphics
    private void drawNotes(Notes n, Graphics g, double w, int topLine, double addH, int noteH) throws RestException {
        if (n.getRelKey() == 13) {
            throw new RestException((int) w, (int) noteH, n.getBeat(), g);
        }
        if (addH >= 6 || addH <= 0) {
            drawNoteLine(addH, g, topLine + ROW_H, w);
        }
        g.setColor(NT);
        if (n.getBeat() < 0.5) {
            g.fillOval((int) w, noteH, NOTE_WIDTH, NOTE_HEIGHT);
        }
        g.drawOval((int) w, noteH, NOTE_WIDTH, NOTE_HEIGHT);
    }

    // MODIFIES: g
    // EFFECTS: add the line for notes that's out of range
    private void drawNoteLine(double addHeight, Graphics g, int bottomLine, double w) {
//        System.out.println("the height:" + addHeight);
        for (int i = (int) addHeight; i > 5; i--) {
            int h = bottomLine - (i - 1) * SMALL;
            g.drawLine((int) (w - NOTE_WIDTH / 2), h, (int) (w + 3 * NOTE_WIDTH / 2), h);
        }
        for (int i = (int) addHeight; i <= 0; i++) {
            int h = bottomLine - (i - 1) * SMALL;
            g.drawLine((int) (w - NOTE_WIDTH / 2), h, (int) (w + 3 * NOTE_WIDTH / 2), h);
        }
    }

    // EFFECTS: return the height relative to center C
    private double calculateHeight(String maj, int octave, int relKey) {
        double height = getStandardHeight();
        if (relKey == 13) {
            height = 3.5;
        } else {
            height += octave * 3.5;
            double a = 0;
            int[] lst = noteMap.get(maj);
            a = lst[relKey];
            height += 0.5 * (a - 1);
        }
        return height;
    }


    // EFFECTS: return the height based on the clef
    private double getStandardHeight() {
        double height = 0;
        if (score.getClef() == 'm') {
            height = 3;
            topCenterC = (int) (TOP_CENTER + SMALL / 2);
        } else if (score.getClef() == 'l') {
            height = 2.5;
            topCenterC = (int) (TOP_CENTER + SMALL);
        } else {
            topCenterC = TOP_CENTER;
        }
        return height;
    }

    // MODIFIES: g
    // EFFECTS: render line as the background of the sheet music
    private void drawBackground(Graphics g) {
        Color bg = g.getColor();
        g.setColor(new Color(0, 0, 0));
        int c = 0;
        for (int y = LARGE + TOP_HEIGHT; y < getHeight(); y += ROW_H) {
            for (int j = 0; j < 5; j++) {
                int y1 = y + j * SMALL;
                g.drawLine(0, y1, getWidth(), y1);
            }
            if (c == 0) {
                drawLeftBar(g, y, 1);
                drawTone(g, 1, c);
            } else {
                drawLeftBar(g, y, 0);
                drawTone(g, 0, c);
            }
            c += 1;
            for (int i = 1; i < MAX_BEAT; i++) {
                int a = (int) (i * getWidth() * (1 - LEFT_PERCENTAGE) / MAX_BEAT + getWidth() * LEFT_PERCENTAGE);
                g.drawLine(a, y, a, y + SMALL * 4);
            }
        }
        g.setColor(bg);
    }

    // EFFECTS: draw the tone on the left bar
    private void drawTone(Graphics g, int i, int c) {
        int[] a = toneMap.get(score.getTone());
        int[] place = new int[]{};
        int w = NOTE_WIDTH * 3 / 2;
        BufferedImage imagesharp = null;
        BufferedImage image = null;
        try {
            imagesharp = ImageIO.read(new File("src/main/ui/imgs/sharp.png"));
            image = ImageIO.read(new File("src/main/ui/imgs/flat2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (a[0] == 1) {
            place = new int[]{10, 7, 11, 8, 5, 9, 6};
            image = imagesharp;
        } else if (a[0] == -1) {
            place = new int[]{6, 9, 5, 8, 11, 7, 10};
        }
        for (int n = 0; n < a[1]; n++) {
            int h = topCenterC + 4 * SMALL - place[n] * NOTE_WIDTH / 2 - NOTE_WIDTH / 4 + ROW_H * c;
            g.drawImage(image, getWidth() / 20 + (2 * i + n) * NOTE_WIDTH / 2, h, w, w, null);
        }
    }

    // MODIFIES: g
    // EFFECTS: draw the signature and sign of the note
    private void drawLeftBar(Graphics g, int y, int c) {
        BufferedImage image = null;
        int s = SMALL * 4;
        int h = y;
        try {
            if (score.getClef() == 'h') {
                image = ImageIO.read(new File("src/main/ui/imgs/high.png"));
                s = SMALL * 6;
                h = y - SMALL;
            } else if (score.getClef() == 'm') {
                image = ImageIO.read(new File("src/main/ui/imgs/medium.png"));
            } else {
                image = ImageIO.read(new File("src/main/ui/imgs/low.png"));
                s = SMALL * 3;
                h = y + SMALL;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        g.drawImage(image, (int) (getWidth() * LEFT_PERCENTAGE * 0.05), h, s, s, null);

        if (c == 1) {
            drawTimeSignature(g, y);
        }
    }

    // MODIFIES: g
    // EFFECT: draw the time signature for the top part
    private void drawTimeSignature(Graphics g, int y) {
        String top = Integer.toString(score.getTimeSignatureIntList()[0]);
        String bottom = Integer.toString(score.getTimeSignatureIntList()[1]);
        Font font = new Font("Times New Roman", Font.BOLD, SMALL * 5 / 2);
        Font font2 = new Font("Times New Roman", Font.PLAIN, (int) (SMALL * 1.7));
        g.setFont(font);
        g.drawString(top, (int) (getWidth() * LEFT_PERCENTAGE * 0.5), y + 2 * SMALL);
        g.drawString(bottom, (int) (getWidth() * LEFT_PERCENTAGE * 0.5), y + 4 * SMALL);
        g.drawString(score.getName(), getWidth() / 2, (int) (LARGE * 0.8));
        g.setFont(font2);
        g.drawString("Tempo:" + Integer.toString(score.getTempo()), (int) (getWidth() * 0.9), (int) (LARGE * 0.6));
        g.drawString("Tone: " + score.getTone(), (int) (getWidth() * 0.9), (int) (LARGE * 0.3));
    }
}