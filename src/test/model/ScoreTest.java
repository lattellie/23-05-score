package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {
    private Notes a4;
    private Notes c2;
    private Score twinkle;
    private Score suppe;
    private Score sixEight;
    private String[] twinkleScore = {"C%10400","C%10400","G%10400","G%10400","A%10400","A%10400","G%10400"};

    private String[] suppeScore = {"F#10400","F#11600","F%11600","F#11600","F%11600","F#10800","D%10800","F#10400"};
    @BeforeEach
    public void setup() {
        twinkle = new Score();
        suppe = new Score("su","Eb","2/4",'h',100);
        sixEight = new Score("waltz","D","6/8",'l',140);
    }

    @Test
    void testConstructor() {
        assertEquals(twinkle.getNotesArray().size(),0);
        assertEquals(twinkle.getClef(),'h');
        assertEquals(twinkle.getNotesArray(),new ArrayList<>());
        assertEquals(twinkle.getName(),"untitled");
        assertEquals(twinkle.getTone(),"C");
        assertEquals(twinkle.getTempo(),120);
        assertEquals(twinkle.getTimeSignature(),"4/4");
        assertEquals(suppe.getClef(),'h');
        assertEquals(suppe.getNotesArray(),new ArrayList<>());
        assertEquals(suppe.getName(),"su");
        assertEquals(suppe.getTone(),"Eb");
        assertEquals(suppe.getTempo(),100);
        assertEquals(suppe.getTimeSignature(),"2/4");
        assertEquals(sixEight.getClef(),'l');
        assertEquals(sixEight.getNotesArray(),new ArrayList<>());
        assertEquals(sixEight.getName(),"waltz");
        assertEquals(sixEight.getTone(),"D");
        assertEquals(sixEight.getTempo(),140);
        assertEquals(sixEight.getTimeSignature(),"6/8");
    }

    @Test
    void testToBarRegular() {
        twinkle.setNotes(Arrays.copyOfRange(twinkleScore, 0,6));
        twinkle.setNotes(new String[]{"G%10200"});
        assertEquals(8,twinkle.getLengthOfScore());
        ArrayList<ArrayList<Notes>> listOfBar = twinkle.toBar();
        assertEquals(listOfBar.size(),3);
        assertEquals(4,listOfBar.get(0).size());
        assertEquals("C 0.25",listOfBar.get(0).get(0).getNotes());
        assertEquals("G 0.25",listOfBar.get(0).get(3).getNotes());
        assertEquals("A 0.25",listOfBar.get(1).get(0).getNotes());
        assertEquals("G 0.5",listOfBar.get(1).get(2).getNotes());
    }

    @Test
    void testToBarMultipleBar() {
        twinkle.setNotes(Arrays.copyOfRange(twinkleScore, 0,6));
        twinkle.setNotes(new String[]{"G%30100"});
        assertEquals(18,twinkle.getLengthOfScore());
        ArrayList<ArrayList<Notes>> listOfBar = twinkle.toBar();
        assertEquals(5,listOfBar.size());
        assertEquals("G 1.0",listOfBar.get(2).get(0).getNotes());
        assertEquals("G 0.5",listOfBar.get(4).get(0).getNotes());
    }

    @Test
    void testToBarNot44() {
        suppe.setNotes(suppeScore);
        suppe.setNotes(suppeScore);
        ArrayList<ArrayList<Notes>> listOfBar = suppe.toBar();
        assertEquals(5,listOfBar.size());
        assertEquals(5,listOfBar.get(0).size());
        assertEquals(5,listOfBar.get(2).size());
        assertEquals(3,listOfBar.get(1).size());
        assertEquals("D 0.125",listOfBar.get(1).get(1).getNotes());
        assertEquals("F 0.0625",listOfBar.get(2).get(2).getNotes());
    }

    @Test
    void testToBarSplitNotes() {
        twinkle.setNotes(twinkleScore);
        twinkle.setNotes(new String[]{"S%10800"});
        twinkle.setNotes(twinkleScore);
        ArrayList<ArrayList<Notes>> listOfBar = twinkle.toBar();
        assertEquals(4,listOfBar.size());
        twinkle.setNotes(new String[]{"S%10800","A%116-1","Eb30800"});
        ArrayList<ArrayList<Notes>> listOfBar2 = twinkle.toBar();
        assertEquals(5,listOfBar2.size());
        assertEquals(6,listOfBar2.get(3).size());
        assertEquals(5,listOfBar2.get(1).size());
        assertEquals(5,listOfBar2.get(2).size());
        assertEquals("C 0.125",listOfBar2.get(1).get(4).getNotes());
        assertEquals("C 0.125",listOfBar2.get(2).get(0).getNotes());
        assertEquals("S 0.125",listOfBar2.get(3).get(3).getNotes());
        assertEquals("Eb 0.1875",listOfBar2.get(3).get(5).getNotes());
        assertEquals("Eb 0.1875",listOfBar2.get(4).get(0).getNotes());
    }

    @Test
    void testGetProperties() {
        ArrayList<Object> x = new ArrayList<Object>();
        x.add(sixEight.getName());
        x.add(sixEight.getTone());
        x.add(sixEight.getTimeSignature());
        x.add(sixEight.getClef());
        x.add(sixEight.getTempo());
        assertEquals(sixEight.getProperties(),x);
    }

    @Test
    void testReTuneNoLimitNumber() {
        twinkle.setNotes(twinkleScore);
        twinkle.reTune(1);
        Notes n = new Notes("C#10400");
        assertEquals(twinkle.getNotesArray().get(1).getNotes(),n.getNotes());
        twinkle.reTune(2);
        assertEquals(twinkle.getNotesArray().get(0).getNotes(),"Eb 0.25");
        twinkle.reTune(-12);
        assertEquals(twinkle.getNotesArray().get(0).getNotes(),".Eb 0.25");
        twinkle.reTune(-12);
        assertEquals("..Eb 0.25",twinkle.getNotesArray().get(0).getNotes());
        twinkle.reTune(-3);
        assertEquals(twinkle.getNotesArray().get(0).getNotes(),"..C 0.25");
        twinkle.reTune(12);
        assertEquals(twinkle.getNotesArray().get(0).getNotes(),".C 0.25");
        twinkle.reTune(23);
        assertEquals(twinkle.getNotesArray().get(0).getNotes(),"B 0.25");
        twinkle.reTune(24);
        assertEquals(twinkle.getNotesArray().get(0).getNotes(),"**B 0.25");
    }

    @Test
    private void testReTuneWithOctaveChange() {
        suppe.setNotes(twinkleScore);
        suppe.reTune(11);
        Notes n2 = new Notes("B%10400");
        assertEquals(suppe.getNotesArray().get(1).getNotes(),n2.getNotes());
        suppe.reTune(2);
        Notes n3 = new Notes("C#104+1");
        assertEquals(suppe.getNotesArray().get(1).getNotes(),n3.getNotes());
        suppe.reTune(-7);
        Notes n4 = new Notes("F#10400");
        assertEquals(suppe.getNotesArray().get(1).getNotes(),n4.getNotes());
        suppe.reTune(-10);
        Notes n5 = new Notes("G#104-1");
        assertEquals(suppe.getNotesArray().get(1).getNotes(),n5.getNotes());
    }

    @Test
    void testSetters() {
        twinkle.setName("twinkle star");
        assertEquals(twinkle.getName(),"twinkle star");
        twinkle.setTempo(60);
        assertEquals(twinkle.getTempo(),60);
        twinkle.setTone("F#");
        assertEquals(twinkle.getTone(),"F#");
        twinkle.setTimeSignature("3/4");
        assertEquals(twinkle.getTimeSignature(),"3/4");
        twinkle.setClef('m');
        assertEquals(twinkle.getClef(),'m');
    }

    @Test
    void testSetNotes() {
        twinkle.setNotes(twinkleScore);
        ArrayList<String> twinkleList = new ArrayList<String>();
        for (String nt: twinkleScore) {
            Notes n = new Notes(nt);
            twinkleList.add(n.getNotes());
        }
        assertEquals(twinkle.getNotesStringArray(),twinkleList);
        assertEquals(twinkle.getNotesStringArray().get(1),new Notes("C%10400").getNotes());
        twinkle.setNotes(twinkleScore);
        assertEquals(14,twinkle.getNotesArray().size());
        twinkle.setNotes(new String[]{"C%10400"});
        assertEquals(15,twinkle.getNotesArray().size());
    }

    @Test
    void testGetLengthOfNotes() {
        twinkle.setNotes(twinkleScore);
        assertEquals(twinkle.getLengthOfScore(),7);
        suppe.setNotes(suppeScore);
        assertEquals(suppe.getLengthOfScore(),4);
        suppe.setNotes(Arrays.copyOfRange(suppeScore, 0,2));
        assertEquals(suppe.getLengthOfScore(),5.25);
        suppe.setNotes(Arrays.copyOfRange(suppeScore, 2,4));
        assertEquals(suppe.getLengthOfScore(),5.75);
        suppe.setNotes(Arrays.copyOfRange(suppeScore, 4,8));
        assertEquals(suppe.getLengthOfScore(),8);
        twinkle.setNotes(new String[]{"C%10100"});
        assertEquals(twinkle.getLengthOfScore(),11);
    }

    @Test
    void testgetTimeSignatureIntList() {
        assertEquals(twinkle.getTimeSignatureIntList()[1],4);
        assertEquals(twinkle.getTimeSignatureIntList()[0],4);
        assertEquals(suppe.getTimeSignatureIntList()[0],2);
        assertEquals(suppe.getTimeSignatureIntList()[1],4);
        assertEquals(sixEight.getTimeSignatureIntList()[0],6);
        assertEquals(sixEight.getTimeSignatureIntList()[1],8);
    }

    @Test
    void testaddNotes() {
        Notes n = new Notes("A#10400");
        suppe.addNotes(n);
        assertEquals(suppe.getNotesArray().get(0).getOctave(),0);
        assertEquals(suppe.getNotesArray().get(0).getBeat(),0.25f);
        assertEquals(suppe.getNotesArray().get(0).getRelKey(),10);
    }
    @Test
    void testToJson() {
        twinkle.setNotes(Arrays.copyOfRange(twinkleScore, 0,2));
        JSONObject j = new JSONObject();
        JSONObject j2 = twinkle.toJson();
        assertEquals(j2.getString("name"),"untitled");
        assertEquals(j2.getInt("tempo"),120);
        assertEquals(j2.getString("timeSignature"),"4/4");
        assertEquals(j2.getString("clef"),"h");
        assertEquals(j2.getString("tonality"),"C");
        JSONArray n = j2.getJSONArray("notes");
        JSONObject n1 = n.getJSONObject(0);
        assertEquals(n1.getInt("octave"),0);
        assertEquals(n1.getInt("key"),0);
        assertEquals(n1.getFloat("beat"),0.25f);
    }

    @Test
    void testShiftRest() {
        twinkle.addNotes(new Notes("S%10400"));
        twinkle.addNotes(new Notes("A#10400"));
        twinkle.reTune(2);
        assertEquals(twinkle.getNotesArray().get(0).getRelKey(),13);
        assertEquals(twinkle.getNotesArray().get(1).getRelKey(),0);
        assertEquals(2,twinkle.getNotesArray().size());
    }
}