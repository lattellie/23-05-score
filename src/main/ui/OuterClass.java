package ui;

import model.Notes;
import model.Score;
import ui.tools.Metronome;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

@SuppressWarnings("ALL")
public class OuterClass extends JFrame implements ActionListener {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 100;
    private Metronome met;
    private JButton finish;
    private JButton start;
    private JButton pause;
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
                Metronome.getMet();
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
        pause = new JButton("stop");
        pause.setActionCommand("stop");
        pause.addActionListener(this);
        tempoInsert = new JTextField();
        tempoInsert.setColumns(20);
        panel.add(tempoInsert);
        panel.add(finish);
        panel.add(start);
        panel.add(pause);
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
                System.out.println(Metronome.getTempo());
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
            Score sc = QuickEditor.getEditedScore(Metronome.getTempo());
            toRobot(sc);
        }
    }

    private void toRobot(Score sc) {
        try {
            Robot robot = new Robot();
            System.out.println("3 ");
            robot.delay(1000); // Delay before starting to type
            System.out.println("2 ");
            robot.delay(1000);
            System.out.println("1 ");
            robot.delay(1000);
            System.out.println("ready to print out");
            ArrayList<Notes> notesArray= sc.getNotesArray();
            notesArray.add(0,new Notes("B%10800"));
            ArrayList<int[]> keyList = new ArrayList<>();
            String tempString = "";
            int[] appeared = new int[]{0,0,0,0,0,0,0};
            int[][] appearlist = new int[7][7];
            Arrays.fill(appearlist,0);
            Integer curBar = 0;
            for (int i=1; i<notesArray.size(); i++) {
//                String[] temp =  allKeyToStringOld(notesArray.get(i-1),notesArray.get(i),curBar,appeared);
                String[] temp =  allKeyToString(notesArray.get(i-1),notesArray.get(i),curBar,appearlist);
                tempString += temp[0];
                curBar = Integer.parseInt(temp[1]);
            }
            System.out.println(tempString);
            actualPrintingOut(tempString, robot);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private String[] allKeyToString(Notes prevNote, Notes note, Integer curBar, int[][] appearlist) {
        String tempString = "";
        int preKey = getActualKey(prevNote);
        int curKey = getActualKey(note);
        int ctrlTime = Math.round((float) (curKey-preKey)/7);
        int noteLength = Math.round(note.getBeat()*16);
        // deal with the note and beat
        String[] temp = noteBeatToString(note, noteLength, curBar, appearlist);
        tempString += temp[0];
        curBar = Integer.parseInt(temp[1]);
        tempString += noteOctaveToString(ctrlTime);
        return new String[]{tempString, String.valueOf(curBar)};
    }

    private String[] noteBeatToString(Notes note, int noteLength, Integer curBar, int[][] appearlist) {
        int relKey = note.getRelKey();
        String tempString = "";
        curBar += noteLength;
        if (curBar >= 17) {
            curBar = curBar - 16;
            Arrays.fill(appearlist,0);
        }

        String noteHeightString = noteHeightToString(note, keyNumList[relKey], appearlist);

        if (noteLength <= 3) {
            tempString = tempString + residual[noteLength] + noteHeightString;
        } else if (noteLength <= 7) {
            tempString = tempString + "5" + noteHeightString + residual[noteLength%4];
        } else if (noteLength <= 11) {
            tempString = tempString + "6" + noteHeightString + residual[noteLength%4];
        } else if (noteLength <= 15) {
            tempString = tempString + "6." + noteHeightString + residual[noteLength%4];
        } else if (noteLength == 16) {
            tempString = tempString + "7" + noteHeightString;
        }
        if (noteLength/4>0 && noteLength%4!=0) {
            tempString += "t";
        }
        return new String[]{tempString, String.valueOf(curBar)};
    }

    private String noteHeightToString(Notes note, int abcedf, int[][] appearlist) {
        int relKey = note.getRelKey();
        int oct = note.getOctave()+3;
        String tempString = "";
        tempString += keyStringList[abcedf];
        if (flatSharpList[relKey] - appearlist[oct][abcedf-1] == 1) {
            tempString += "n";
        } else if (flatSharpList[relKey] - appearlist[oct][abcedf-1] == -1) {
            tempString += "v";
        }
        appearlist[oct][abcedf-1] = flatSharpList[relKey];
        return tempString;
    }

    // curBar: 0~16; reset appeared when curBar
    private String[] allKeyToStringOld(Notes prevNote, Notes note, Integer curBar, int[] appeared) {
        String tempString = "";
        int preKey = getActualKey(prevNote);
        int curKey = getActualKey(note);
        int ctrlTime = Math.round((float) (curKey-preKey)/7);
        int noteLength = Math.round(note.getBeat()*16);
        // deal with the note and beat
        String[] temp = noteBeatToStringOld(note.getRelKey(), noteLength, curBar, appeared);
        tempString += temp[0];
        curBar = Integer.parseInt(temp[1]);
        tempString += noteOctaveToString(ctrlTime);
        return new String[]{tempString, String.valueOf(curBar)};
    }

    private String[] noteBeatToStringOld(int relKey, int noteLength, Integer curBar, int[] appeared) {
        String tempString = "";
        curBar += noteLength;
        if (curBar >= 17) {
            curBar = curBar - 16;
            Arrays.fill(appeared,0);
        }

        String noteHeightString = noteHeightToStringOld(relKey, keyNumList[relKey], appeared);

        if (noteLength <= 3) {
            tempString = tempString + residual[noteLength] + noteHeightString;
        } else if (noteLength <= 7) {
            tempString = tempString + "5" + noteHeightString + residual[noteLength%4];
        } else if (noteLength <= 11) {
            tempString = tempString + "6" + noteHeightString + residual[noteLength%4];
        } else if (noteLength <= 15) {
            tempString = tempString + "6." + noteHeightString + residual[noteLength%4];
        } else if (noteLength == 16) {
            tempString = tempString + "7" + noteHeightString;
        }
        if (noteLength/4>0 && noteLength%4!=0) {
            tempString += "t";
        }
        return new String[]{tempString, String.valueOf(curBar)};
    }

    private String noteHeightToStringOld(int relKey, int abcedf, int[] appeared) {
        String tempString = "";
        tempString += keyStringList[abcedf];
        if (flatSharpList[relKey] - appeared[abcedf-1] == 1) {
            tempString += "n";
        } else if (flatSharpList[relKey] - appeared[abcedf-1] == -1) {
            tempString += "v";
        }
        appeared[abcedf-1] = flatSharpList[relKey];
        return tempString;
    }

    private static int[] keyNumList = new int[]{1, 1, 2, 3, 3, 4, 4, 5, 5, 6, 7, 7};
    private static int[] flatSharpList = new int[]{0,1,0,-1,0,0,1,0,1,0,-1,0};
    private static String[] keyStringList = new String[]{"s","c","d","e","f","g","a","b"};
    private static String[] residual = new String[]{"","3","4","4."};

    private String noteOctaveToString(int ctrlTime) {
        String tempString ="";
        if (ctrlTime > 0) {
            tempString += "[x"+"n".repeat(ctrlTime)+"]x";
        } else if (ctrlTime < 0) {
            tempString += "[x"+"v".repeat(-ctrlTime)+"]x";
        }
        return tempString;
    }


    private int getActualKey(Notes note) {
        if (note.getRelKey() == 13) {
            return -1;
        } else {
            return keyNumList[note.getRelKey()] + (note.getOctave()+3)*7;
        }
    }

// the rest is for turning string to robot
    private void actualPrintingOut(String tempString, Robot robot) {
        char[] strArray = tempString.toLowerCase(Locale.ROOT).toCharArray();
        Map<Character, Integer> charDict = new HashMap<>();
        setCharDict(charDict);
        Set<Character> charSet = charDict.keySet();
        robot.delay(3000);
        // half = 1: [ occur; half = -1: ] occur
        int half = 0;
        for (char chr:strArray) {
            if (chr == '[') {
                half = 1;
            } else if (chr == ']'){
                half = -1;
            } else if (charSet.contains(chr)){
                if (half == 1) {
                    robot.keyPress(charDict.get(chr));
                    half = 0;
                } else if (half == -1) {
                    robot.keyRelease(charDict.get(chr));
                    half = 0;
                } else if (half == 0) {
                    robot.keyPress(charDict.get(chr));
                    robot.keyRelease(charDict.get(chr));
                }
            }
        }
        System.out.println("finish printing");
    }

    private void setCharDict(Map<Character, Integer> charDict) {
        char[] charNoteList = new char[]{'c','d','e','f','g','a','b'};
        int[] intNoteList = new int[]{67,68,69,70,71,65,66};
        char[] charOtherList = new char[]{'l','r','n','v','x'};
        int[] intOtherList = new int[]{37,39,38,40,17};
        char[] charNumList = new char[]{'1','2','3','4','5','6','7','.'};
        int[] intNumList = new int[]{49,50,51,52,53,54,55,46};
        for (int i=0;i<charNoteList.length;i++) {
            charDict.put(charNoteList[i],intNoteList[i]);
        }
        for (int i=0;i<charOtherList.length;i++) {
            charDict.put(charOtherList[i],intOtherList[i]);
        }
        for (int i=0;i<charNumList.length;i++) {
            charDict.put(charNumList[i],intNumList[i]);
        }

    }

    private String printArray(int[] appeared) {
        StringBuilder sb = new StringBuilder();
        for (int element : appeared) {
            sb.append(element);
        }
        String str = sb.toString();
        return str;
    }
}

