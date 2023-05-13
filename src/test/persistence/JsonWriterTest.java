package persistence;

import model.Notes;
import model.Score;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// Adapted from "JsonSerializationDemo" by CPSC210
public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Score sco = new Score();
            JsonWriter writer = new JsonWriter("./data/\0illegal:Filename.json");
            writer.open();
            fail("IOException was expected");
        } catch (FileNotFoundException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyScore() {
        try {
            Score sc = new Score("presto","B","3/4",'l',170);
            JsonWriter writer = new JsonWriter("./data/emptyScoreTest.json");
            writer.open();
            writer.write(sc);
            writer.close();

            JsonReader reader = new JsonReader("./data/emptyScoreTest.json");
            Score sco = reader.read();
            assertEquals("presto",sco.getName());
            assertEquals(0,sco.getNotesArray().size());
            assertEquals("3/4",sco.getTimeSignature());
            assertEquals(170,sco.getTempo());
            assertEquals("B",sco.getTone());
            assertEquals('l',sco.getClef());
        } catch (FileNotFoundException e) {
            fail("FileNotFound should not be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    void testWriterGeneralScore() {
        try {
            Score sc = new Score("presto","B","3/4",'l',170);
            sc.addNotes(new Notes("Ab10400"));
            sc.addNotes(new Notes("F#401+1"));
            sc.addNotes(new Notes("E%116-1"));
            JsonWriter writer = new JsonWriter("./data/generalScoreTest.json");
            writer.open();
            writer.write(sc);
            writer.close();

            JsonReader reader = new JsonReader("./data/generalScoreTest.json");
            Score sco = reader.read();
            assertEquals("presto",sco.getName());
            assertEquals(3,sco.getNotesArray().size());
            assertEquals("3/4",sco.getTimeSignature());
            assertEquals(170,sco.getTempo());
            assertEquals("B",sco.getTone());
            assertEquals('l',sco.getClef());
            assertEquals(4,sco.getNotesArray().get(2).getRelKey());
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}
