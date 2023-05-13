package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

// add new Score
public class Score implements Writable {
    private ArrayList<Notes> notes;
    private String name;
    private String timeSignature;
    private String tone; //tonality
    private char clef;
    private int tempo;

    //EFFECTS: create a new standard score object
    public Score() {
        this.notes = new ArrayList<Notes>();
        this.clef = 'h';
        this.timeSignature = "4/4";
        this.tempo = 120;
        this.name = "untitled";
        this.tone = "C";
    }

    //REQUIRES: tones fits style [A~G]or[a~g] + [#,b,%]
    //          clef is one of 'h','m','l'
    //          timeSignature fits style int/[int that's 2^n]
    //EFFECTS: create a new specified score object
    public Score(String nm, String tn, String ts, char cl, int tp) {
        this.notes = new ArrayList<Notes>();
        this.clef = cl;
        this.timeSignature = ts;
        this.tempo = tp;
        this.name = nm;
        this.tone = tn;
    }

    // EFFECTS: output an arraylist of scores, each element is a bar
    public ArrayList<ArrayList<Notes>> toBar() {
        //ArrayList<ArrayList<Notes>> bar = new ArrayList<ArrayList<Notes>>();
        //float beatcount = 0;
        float beatperbar = (float) this.getTimeSignatureIntList()[0] / (float) this.getTimeSignatureIntList()[1];
        ArrayList<Notes> singlebar = new ArrayList<Notes>();
        ArrayList<ArrayList<Notes>> bar = assignBar(new ArrayList<ArrayList<Notes>>(), beatperbar, singlebar);
        bar.add(singlebar);
        return bar;
    }

    // iterate through the score list and make bar.
    private ArrayList<ArrayList<Notes>> assignBar(ArrayList<ArrayList<Notes>> bar, float bpb, ArrayList<Notes> oneBar) {
        float beatcnt = 0.0f;
        for (Notes n : this.notes) {
            float rem = bpb - beatcnt;
            beatcnt += n.getBeat();
            if (n.getBeat() < rem) {
                oneBar.add(n);
            } else {
                float float1 = (n.getBeat() - rem) / bpb;
                int num = (int) float1;
                float finalBeat = beatcnt - bpb - bpb * (float) num;
                oneBar.add(new Notes(n.getAbsKey(), rem, n.getOctave()));
                addToBar(bar, oneBar);
                for (int c = num; c > 0; c--) {
                    oneBar.add(new Notes(n.getAbsKey(), bpb, n.getOctave()));
                    addToBar(bar, oneBar);
                }
                if (finalBeat > 0) {
                    oneBar.add(new Notes(n.getAbsKey(), finalBeat, n.getOctave()));
                }
                beatcnt = finalBeat;
            }
        }
        return bar;
    }

    // helper function of creating a bar
    private void addToBar(ArrayList<ArrayList<Notes>> bar, ArrayList<Notes> oneBar) {
        ArrayList<Notes> savebar = new ArrayList<Notes>(oneBar);
        bar.add(savebar);
        oneBar.clear();
    }

    // REQUIRES: time signature is in format int/[int that's 2^n]
    // EFFECTS: get the time signature in integer array [upper, lower]
    public int[] getTimeSignatureIntList() {
        String[] tsArray = this.timeSignature.split("/");
        return new int[]{Integer.parseInt(tsArray[0]), Integer.parseInt(tsArray[1])};
    }

    // EFFECTS: get the length (how many beats are in there) of the score
    public float getLengthOfScore() {
        float count = 0;
        for (Notes n : this.notes) {
            count = count + n.getBeat();
        }
        return count * 4;
    }


    // Getters and setters

    // EFFECTS: get all the attributes at once
    public ArrayList<Object> getProperties() {
        // return [str(name), str(tone), str(time), char(clef), int(tempo)
        ArrayList<Object> ary = new ArrayList<Object>();
        ary.add(this.name);
        ary.add(this.tone);
        ary.add(this.timeSignature);
        ary.add(this.clef);
        ary.add(this.tempo);
        return ary;
    }


    // REQUIRES: int in range(-12,12)
    // MODIFIES: this
    // EFFECTS: set the tone relatively
    public void reTune(int diff) {
        for (Notes n : this.notes) {
            int a = n.getRelKey();
            if (a != 13) {
                int r = a + diff;
                if (r >= 12) {
                    n.setOctave(n.getOctave() + r / 12);
                } else if (r < 0) {
                    n.setOctave(n.getOctave() - r / 12 - 1);
                }
                n.setKey((r + 12) % 12);
            }
        }
        EventLog.getInstance().logEvent(new Event("Retuned " + Integer.toString(diff)));
    }


    // REQUIRES: each element in the array is in format "__key___beat__octave" ex: A%301+3 Bb116-2 C#30800
    // EFFECTS: set the arraylist
    // MODIFIES: this
    public void setNotes(String[] nt) {
        for (String s : nt) {
            this.notes.add(new Notes(s));
        }
    }

    // EFFECTS: add one note to the list
    // MODIFIES: this
    public void addNotes(Notes n) {
        this.notes.add(n);
        EventLog.getInstance().logEvent(new Event("Notes added: " + n.getNotes()));
    }

    // EFFECTS: get the arraylist of notes
    public ArrayList<Notes> getNotesArray() {
        return notes;
    }

    // EFFECTS: get the arraylist of notes in string format for testing
    public ArrayList<String> getNotesStringArray() {
        ArrayList<String> nlist = new ArrayList<String>();
        for (Notes n : notes) {
            nlist.add(n.getNotes());
        }
        return nlist;
    }

    // REQUIRES: inputted string fits style [A~G]or[a~g] + [#,b,%]
    // MODIFIES: this
    // EFFECTS: set the tone
    public void setTone(String tn) {
        this.tone = tn;
        EventLog.getInstance().logEvent(new Event("Set tone to: " + tn));
    }

    // EFFECTS: get the tone
    public String getTone() {
        return tone;
    }

    // REQUIRES: int is in range (30,280)
    // MODIFIES: this
    // EFFECT: set the tempo
    public void setTempo(int tmp) {
        this.tempo = tmp;
        EventLog.getInstance().logEvent(new Event("Set tempo to: " + Integer.toString(tmp)));
    }

    // EFFECTS: get the tempo
    public int getTempo() {
        return tempo;
    }

    // MODIFIES: this
    // EFFECTS: set the name
    public void setName(String nm) {
        this.name = nm;
        EventLog.getInstance().logEvent(new Event("Set name to: " + nm));
    }

    // EFFECTS: get the name
    public String getName() {
        return name;
    }

    // REQUIRES: char is one of 'l','m','h'
    // MODIFIES: this
    // EFFECTS: set the name
    public void setClef(char cf) {
        this.clef = cf;
        EventLog.getInstance().logEvent(new Event("Set clef to: " + cf));
    }

    // EFFECTS: get the clef
    public char getClef() {
        return clef;
    }

    // REQUIRES: time signature fits the style int/2^n
    // MODIFIES: this
    // EFFECTS: set the time signature
    public void setTimeSignature(String ts) {
        this.timeSignature = ts;
        EventLog.getInstance().logEvent(new Event("Set time signature to: " + ts));
    }

    // EFFECTS: get the time signature
    public String getTimeSignature() {
        return timeSignature;
    }


    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("notes", notesToJson());
        json.put("name", name);
        json.put("timeSignature", timeSignature);
        json.put("tonality", tone);
        String c = String.valueOf(clef);
        json.put("clef", c);
        json.put("tempo", tempo);
        return json;
    }

    // EFFECTS: returns notes in this score as a JSON array
    private JSONArray notesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Notes n : notes) {
            jsonArray.put(n.toJson());
        }
        return jsonArray;
    }
}
