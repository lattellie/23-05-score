package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// add new Notes
public class Notes implements Writable {
    private int octave;
    private int key;
    private float beat;
    private static final String[] STD = {"C", "C#", "D", "Eb", "E", "F", "F#", "G", "G#", "A", "Bb", "B", "C", "S"};
    private static final List<String> STANDARD_LIST = new ArrayList<String>(Arrays.asList(STD));
    private static final String[] SHARP = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B", "C", "S"};
    private static final List<String> SHARP_LIST = new ArrayList<String>(Arrays.asList(SHARP));
    private static final String[] FLAT = {"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B", "C", "S"};
    private static final List<String> FLAT_LIST = new ArrayList<String>(Arrays.asList(FLAT));
    private static final List<String> C_MAJOR = Arrays.asList(STD);


    // REQUIRES: String ky fits the style [A~G] + '#'/"b"/"B"/"%"
    //           octave is in interval [-3,3]
    //           bt is "int/2^n"
    // EFFECTS: create a note with absolute key, specified octave
    public Notes(String key, String bt, int oct) {
        this.key = interpretKey(key);
        this.octave = oct;
        this.beat = interpretBeatSlash(bt);
    }

    // REQUIRES: int key <= 12, oct is in interval [-3,3]
    // EFFECTS: create a note with relative key, specified octave, specified beat
    public Notes(int key, float bt, int oct) {
        this.key = key;
        this.beat = bt;
        this.octave = oct;
    }

    // REQUIRES: String ky fits the style [A~G] + '#'/"b"/"B"/"%"
    //           octave is in interval [-3,3]
    //           bt is a finite decimal number
    // EFFECTS: create a note with absolute key, specified octave
    public Notes(String ky, float bt, int oct) {
        this.key = interpretKey(ky);
        this.octave = oct;
        this.beat = bt;
    }

    // REQUIRES: bt is in the format "int/2^n"
    // EFFECTS: return an array of [int,2^n]
    private float interpretBeatSlash(String bt) {
        String[] b = bt.split("/");
        float number = Integer.parseInt(b[0]);
        if (b.length == 2) {
            return number / (float) Integer.parseInt(b[1]);
        } else {
            return number;
        }
    }

    // REQUIRES: bt is in the format "int/2^n"
    // EFFECTS: return an array of [int,2^n]
    private float interpretBeatNum(String bt) {
        float front = Integer.parseInt(bt.substring(0, 1));
        float bottom = Integer.parseInt(bt.substring(1, 3));
        String[] b = bt.split("/");
        return front / bottom;
    }

    // REQUIRES: String ky fits the style [A~G] + '#'/"b"/"B"/"%"
    // EFFECTS: return the index of key
    private int interpretKey(String ky) {
        if (ky.startsWith("S")) {
            return 13;
        } else if (ky.endsWith("#")) {
            return SHARP_LIST.indexOf(ky);
        } else if (ky.endsWith("b")) {
            return FLAT_LIST.indexOf(ky);
        } else {
            return FLAT_LIST.indexOf(ky.substring(0, 1));
        }
    }


    // REQUIRES: String str is a string that's in format "__key___beat__octave" ex: A%301+3 Bb116-2 C#30800
    // EFFECTS: create a note with absolute key, standard octave
    public Notes(String str) {
        this.beat = interpretBeatNum(str.substring(2, 5));
        this.key = interpretKey(str.substring(0, 2));
        this.octave = Integer.parseInt(str.substring(5));

    }

    // EFFECTS: get the notes as a string in format "**C# 1/4" "..F 2" "Ab 1/16" etc
    public String getNotes() {
        StringBuilder note;
        note = new StringBuilder();
        if (this.octave < 0) {
            for (int a = -this.octave; a > 0; a--) {
                note.append(".");
            }
        } else {
            for (int a = this.octave; a > 0; a--) {
                note.append("*");
            }
        }
        note.append(this.getAbsKey());
        note.append(" ");
        note.append(this.beat);
        return note.toString();
    }

    // setters and getters
    // EFFECTS: get the octave
    public int getOctave() {
        return octave;
    }

    // REQUIRES: octave is in the interval [-3,3]
    // MODIFIES: this
    // EFFECTS: set the octave
    public void setOctave(int oct) {
        this.octave = oct;
    }

    // EFFECTS: get the relative key
    public int getRelKey() {
        return this.key;
    }

    // EFFECTS: get the absolute key
    public String getAbsKey() {
        return STD[this.key];
    }

    // REQUIRES: key is in the interval [-3,3]
    // MODIFIES: this
    // EFFECTS: set the key
    public void setKey(int ky) {
        this.key = ky;
    }

    // REQUIRES: key ky fits the style [A~G] + '#'/"b"/"%"
    // MODIFIES: this
    // EFFECTS: set the key
    public void setKey(String ky) {
        this.key = interpretKey(ky);
    }

    // EFFECTS: get the beat
    public float getBeat() {
        return this.beat;
    }

    // REQUIRES: String is int/2^n
    // MODIFIES: this
    // EFFECTS: set the beat
    public void setBeat(String b) {
        this.beat = interpretBeatSlash(b);
    }


    // EFFECT: returns this as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("octave", octave);
        json.put("key", key);
        json.put("beat", beat);
        return json;
    }
}
