package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NotesTest {
    private Notes a4;
    private Notes bFlat1;
    private Notes dSharp04;
    private Notes eFlat016;
    private Notes fSharp2;
    private Notes g02;
    private Notes cSharp04;
    private Notes low1;
    private Notes break1;
    private Notes break2;

    @BeforeEach
    void setup() {
        low1 = new Notes("Eb101-1");
        a4 = new Notes("A", "4",0);
        bFlat1 = new Notes("Bb", "1",0);
        dSharp04 = new Notes("D#", 0.25f,0);
        eFlat016 = new Notes("Eb","1/16",0);
        fSharp2 = new Notes("F#20100");
        g02 = new Notes("G%10200");
        cSharp04 = new Notes("C#10400");
        break1 = new Notes("S%10400");
        break2 = new Notes("S","4",0);
    }

    @Test
    void testConstructorOctave() {
        Notes low2 = new Notes("C#","1/8",-2);
        Notes high2 = new Notes("D","2",2);
        Notes high1 = new Notes("C#201+1");
        assertEquals(a4.getOctave(),0);
        assertEquals(low2.getOctave(),-2);
        assertEquals(low1.getOctave(),-1);
        assertEquals(high2.getOctave(),2);
        assertEquals(high1.getOctave(),1);
    }
    @Test
    void testGetNotes() {
        Notes high1 = new Notes("C#102+1");
        assertEquals(cSharp04.getNotes(),"C# 0.25");
        assertEquals(a4.getNotes(),"A 4.0");
        assertEquals(high1.getNotes(),"*C# 0.5");
        assertEquals(low1.getNotes(),".Eb 1.0");
    }

    @Test
    void testBreak() {
        assertEquals("S 0.25",break1.getNotes());
        assertEquals("S",break1.getAbsKey());
        assertEquals(13,break1.getRelKey());
        assertEquals(0.25,break1.getBeat());
        assertEquals(0,break1.getOctave());
        assertEquals("S 4.0",break2.getNotes());
        assertEquals("S",break2.getAbsKey());
        assertEquals(13,break2.getRelKey());
        assertEquals(4,break2.getBeat());
        assertEquals(0,break2.getOctave());
    }

    @Test
    void testSetOctave() {
        a4.setOctave(3);
        assertEquals(a4.getOctave(),3);
        cSharp04.setOctave(-3);
        assertEquals(cSharp04.getOctave(),-3);
        low1.setOctave(0);
        assertEquals(low1.getOctave(),0);
    }
    @Test
    void testRelKey(){
        Notes c1 = new Notes("C","1/16",0);
        assertEquals(c1.getRelKey(),0);
        assertEquals(dSharp04.getRelKey(),3);
        assertEquals(g02.getRelKey(),7);
        assertEquals(bFlat1.getRelKey(),10);
    }
    @Test
    void testAbsKey(){
        assertEquals(eFlat016.getAbsKey(),"Eb");
        assertEquals(fSharp2.getAbsKey(),"F#");
        assertEquals(g02.getAbsKey(),"G");
    }
    @Test
    void testSetKey() {
        a4.setKey(8);
        assertEquals(a4.getRelKey(),8);
        assertEquals(a4.getAbsKey(),"G#");
        dSharp04.setKey("F");
        assertEquals(dSharp04.getAbsKey(),"F");
        assertEquals(dSharp04.getRelKey(),5);
        g02.setKey("Ab");
        assertEquals(g02.getAbsKey(),"G#");
        assertEquals(g02.getRelKey(),8);
        assertEquals(low1.getAbsKey(),"Eb");
        assertEquals(low1.getRelKey(),3);
    }
    @Test
    void testSetBeat() {
        bFlat1.setBeat("1/8");
        assertEquals(0.125,bFlat1.getBeat());
        assertEquals(0.25,cSharp04.getBeat());
        cSharp04.setBeat("4");
        assertEquals(4,cSharp04.getBeat());

    }
    @Test
    void testNewConstructor() {
        Notes const2 = new Notes(1,0.25f,+3);
        assertEquals(const2.getAbsKey(),"C#");
        assertEquals(const2.getRelKey(),1);
        assertEquals(const2.getOctave(),3);
        Notes const3 = new Notes(4,2f,-3);
        assertEquals(const3.getAbsKey(),"E");
        assertEquals(const3.getRelKey(),4);
        assertEquals(const3.getOctave(),-3);
    }
    @Test
    void testToJson() {
        Notes const2 = new Notes(1,0.25f,+3);
        JSONObject j = new JSONObject();
        j.put("octave", 3);
        j.put("key", 1);
        j.put("beat", 0.25f);
        JSONObject j2 = const2.toJson();
        assertEquals(j.toString(),j2.toString());
    }
}
