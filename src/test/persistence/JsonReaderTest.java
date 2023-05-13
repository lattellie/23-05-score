package persistence;

import model.Score;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// Adapted from "JsonSerializationDemo" by CPSC210
public class JsonReaderTest {
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Score sco = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderGeneralScore() {
        JsonReader reader = new JsonReader("./data/generalScore.json");
        try {
            Score sco = reader.read();
            assertEquals(sco.getTempo(),120);
            assertEquals(sco.getName(),"untitled");
            assertEquals(sco.getTimeSignature(),"4/4");
            assertEquals(sco.getTone(),"C");
            assertEquals(sco.getClef(),'h');
            assertEquals(sco.getNotesArray().get(1).getNotes(),"**C# 0.0625");
        } catch (IOException e) {
            fail("couldn't read from file");
        }
    }

    @Test
    void testEmptyScore() {
        JsonReader reader = new JsonReader("./data/emptyScore.json");
        try {
            Score s = reader.read();
            assertEquals("reverse", s.getName());
            assertEquals(0, s.getNotesArray().size());
            assertEquals(s.getClef(),'h');
            assertEquals(s.getTempo(),120);
            assertEquals(s.getTone(),"Eb");
            assertEquals(s.getTimeSignature(),"4/4");
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
