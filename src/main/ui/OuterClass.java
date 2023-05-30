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
    private static final int WIDTH = 550;
    private static final int HEIGHT = 100;
    private Metronome met;
    private JButton finish;
    private JButton start;
    private JButton pause;
    private JPanel panel;
    private JTextField tempoInsert;
    private JComboBox<Integer> beatInsert;
    private JComboBox<Integer> flatSharpNumber;
    private JComboBox<Character> flatSharp;
    private JComboBox<String> clef;
    private JLabel outputstring;
    private static Thread piano;
    private static Thread metro;
    private int[] appeared = new int[]{0,0,0,0,0,0,0};;
    // appeared is 0000000 if it's C maj; 1 if #, -1 if b
    private String[] sixteenToString = new String[]{"","3","4","4.","5","","","","6","","","","6.","","","","7"};
    private static int[] keyNumList = new int[]{1, 1, 2, 3, 3, 4, 4, 5, 5, 6, 7, 7};
    private static int[] flatSharpList = new int[]{0,1,0,-1,0,0,1,0,1,0,-1,0};
    private static String[] keyStringList = new String[]{"s","c","d","e","f","g","a","b"};
    private static String[] residual = new String[]{"","3","4","4."};
    private Notes previousNote = null;
    private Integer curBar = 0;
    private int sixteenthAmount = 16;
    private
    int[][] appearlist = new int[7][7];

    public OuterClass() {
        super("button window");

        metro = new Thread(new Runnable() {
            @Override
            public void run() {
                resetArray(appearlist);
                Metronome.getMet();
            };
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
        pause = new JButton("pause");
        pause.setActionCommand("pause");
        pause.addActionListener(this);
        tempoInsert = new JTextField();
        tempoInsert.setColumns(10);
        beatInsert = new JComboBox<>(new Integer[]{1,2,4});
        flatSharpNumber = new JComboBox<>(new Integer[]{0,1,2,3,4,5,6,7});
        flatSharp = new JComboBox<>(new Character[]{'#','b'});
        clef = new JComboBox<>(new String[]{"Treble","Bass"});
        outputstring = new JLabel();
        outputstring.setText("enter beat for metronome & how many 16th note is in that beat (default = 1)");
        panel.add(outputstring);
        panel.add(tempoInsert);
        panel.add(beatInsert);
        panel.add(flatSharpNumber);
        panel.add(flatSharp);
        panel.add(clef);
        panel.add(finish);
        panel.add(start);
        panel.add(pause);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand()=="start") {
            if (previousNote == null) {
                if (clef.getSelectedItem()=="Treble") {
                    previousNote = new Notes("B%10800");
                } else if (clef.getSelectedItem()=="Bass"){
                    previousNote = new Notes("D%108-1");
                }
            }
            int tem;
            int beat = (int) beatInsert.getSelectedItem();
            setFlatSharp();
            try {
                tem = Integer.parseInt(tempoInsert.getText());
            } catch (NumberFormatException er) {
                tem = 120;
            }
            Metronome.setTempo(tem,beat);
            try {
                System.out.println(Metronome.getTempo());
                metro.start();
            } catch (IllegalThreadStateException err) {
                metro.resume();
            }
            System.out.println("start");
        } else if (e.getActionCommand() == "pause") {
            System.out.println("pause");
            metro.suspend();
            Score sc = QuickEditor.getEditedScore(Metronome.getTempo()*4);
            toRobot(sc);
            QuickEditor.getNoteList().clear();
            QuickEditor.getTimeList().clear();
        } else if (e.getActionCommand() == "finish") {
            metro.stop();
            Score sc = QuickEditor.getEditedScore(Metronome.getTempo()*4);
            toRobot(sc);
            System.exit(0);
        }
    }

    private void setFlatSharp() {
        // 1 if # -1 if b
        int flatSharpOne = -2 * flatSharp.getSelectedIndex() + 1;
        int fsNum = (int) flatSharpNumber.getSelectedItem();
//        int[] sharp = new int[]{4,1,5,2,6,3,7};
//        int[] flat = new int[]{7,3,6,2,5,1,4};
        int[][] fsIndex = new int[][]{{4,1,5,2,6,3,7},{7,3,6,2,5,1,4}};
        int[][] fsNumIndex = new int[][]{{1,1,2,2,3,4,4,5,5,6,6,7},{1,2,2,3,3,4,5,5,6,6,7,7}};
        int[][] fsListIndex = new int[][]{{0,1,0,1,0,0,1,0,1,0,1,0},{0,-1,0,-1,0,0,-1,0,-1,0,-1,0}};
        int[] fsCur = fsIndex[flatSharp.getSelectedIndex()];
        if (fsNum != 0) {
            keyNumList = fsNumIndex[flatSharp.getSelectedIndex()];
            flatSharpList = fsListIndex[flatSharp.getSelectedIndex()];
        }
        for (int i=0;i<fsNum;i++) {
            appeared[fsCur[i]-1] = flatSharpOne;
        }
        System.out.println(printArray(appeared));

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
            notesArray.add(0,previousNote);
            ArrayList<int[]> keyList = new ArrayList<>();
            String tempString = "";
            for (int i=1; i<notesArray.size(); i++) {
//                String[] temp =  allKeyToStringOld(notesArray.get(i-1),notesArray.get(i),curBar,appeared);
                String[] temp =  allKeyToString(notesArray.get(i-1),notesArray.get(i),curBar,appearlist);
                tempString += temp[0];
                curBar = Integer.parseInt(temp[1]);
            }
            previousNote = notesArray.get(notesArray.size()-1);
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
        String[] temp = new String[]{};
        if (curKey == -1) {
            int prevNoteRel = prevNote.getRelKey();
            int preNoteABC = keyNumList[prevNoteRel]; // 1:C, 2:D ...etc
            // set note's height to previous Note (without any modifying up / down)
            int toBefs = appearlist[prevNote.getOctave()][preNoteABC-1];
            // 1 if should be sharp, 0 if should be normal, -1 if should be flat
            int newSetNote = prevNoteRel;
            for (int i = 0; i<12; i++) {
                if (keyNumList[i] == preNoteABC && flatSharpList[i] == toBefs) {
                    newSetNote = i;
                }
            }
            note.setKey(newSetNote);
            note.setOctave(prevNote.getOctave());
            temp = dealWithRest(newSetNote, curBar, noteLength);
            tempString += temp[0];
            curBar = Integer.parseInt(temp[1]);
        } else {
            temp = noteBeatToString(note, noteLength, curBar, appearlist, ctrlTime);
            tempString += temp[0];
            curBar = Integer.parseInt(temp[1]);
            tempString += noteOctaveToString(ctrlTime);
        }
        return new String[]{tempString, String.valueOf(curBar)};
    }

    // given the note height we should set{0~12}, the current position, the length of the note
    // return {the string we should type, the new position}
    // space 32 back 8 left 37 right 39
    private String[] dealWithRest(int newSetNote, Integer n, int r) {
        ArrayList<Integer> splittedRest = new ArrayList<>();
        int l = n+r;// l = n + r
        if (n==0) {
            splittedRest.addAll(fnForStartZero(r));
        } else if (r == 1) {
            splittedRest.add(1);
        } else if (r == 2) {
            splittedRest.add(1);
            splittedRest.add(1);
        } else if (l<sixteenthAmount) {
            SplittingRest(n, splittedRest, l, 4);
        } else {
            SplittingRest(n, splittedRest, l, sixteenthAmount);
        }
        String tempString = restArrayToString(newSetNote, splittedRest);
        return new String[]{tempString, String.valueOf(l%sixteenthAmount)};
    }

    private String restArrayToString(int newSetNote, ArrayList<Integer> splittedRest) {
        int a = keyNumList[newSetNote]; // 1~7
        String temp = "";
        String keyString = keyStringList[a];// 'c'...~'b'
        for (int length:splittedRest) {
            temp += sixteenToString[length];
            // length 1: 1/16, 2:1/8 ...etc
            temp += keyString;
            temp += "<";
            temp += "r";
        }
        return temp;
    }

    private void SplittingRest(Integer n, ArrayList<Integer> splittedRest, int l, int unit) {
        int end = l % unit;
        int mid = (l -end- n)/unit;
        int str = (l -end- n -unit*mid);
        splittedRest.addAll(reverseOrder(fnForStartZero(str)));
        for (int k = 0; k < mid; k++) {
            splittedRest.add(unit);
        }
        splittedRest.addAll(fnForStartZero(end));
    }

    private ArrayList<Integer> reverseOrder(ArrayList<Integer> lst) {
        Collections.reverse(lst);
        return lst;
    }
    private ArrayList<Integer> fnForStartZero(int i) {
        ArrayList<Integer> returnArray = new ArrayList<>();
        for(int n = 8; n >=1; n/=2) {
            if(i%(2*n)/n==1) {
                returnArray.add(n);
            }
        }
        return returnArray;
    }

    // return {the string we need for typing that note, the current bar}
    private String[] noteBeatToString (Notes note, int noteLength, Integer curBar, int[][] appearlist, int ctrlTime) {
        int relKey = note.getRelKey();
        String tempString = "";
        curBar += noteLength;
        curBar = checkResetCurBar(curBar, appearlist);
        int oct = note.getOctave()+3;
        int abcedf = keyNumList[relKey];
        String noteHeightString = "";
        noteHeightString += keyStringList[abcedf];
        if (flatSharpList[relKey] - appearlist[oct-ctrlTime][abcedf-1] == 1) {
            noteHeightString += "n";
        } else if (flatSharpList[relKey] - appearlist[oct-ctrlTime][abcedf-1] == -1) {
            noteHeightString += "v";
        }
        appearlist[oct][abcedf-1] = flatSharpList[relKey];

//        String noteHeightString = noteHeightToString(note, keyNumList[relKey], appearlist, ctrlTime);

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

    private Integer checkResetCurBar(Integer curBar, int[][] appearlist) {
        if (curBar > sixteenthAmount) {
            curBar = curBar - sixteenthAmount;
            resetArray(appearlist);
        }
        return curBar;
    }

    private String noteHeightToString(Notes note, int abcedf, int[][] appearlist, int ctrlTime) {
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
        char[] charOtherList = new char[]{'l','r','n','v','x','<'};
        int[] intOtherList = new int[]{37,39,38,40,17,8};
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
    private void resetArray(int[][] appearlist) {
        for (int i = 0; i < 7; i++) {
            appearlist[i] = Arrays.copyOf(appeared,7);
        }
        System.out.println("array resetted");
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

