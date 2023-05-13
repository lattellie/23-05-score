package persistence;

import model.Notes;
import model.Score;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Adapted from "JsonSerializationDemo" by CPSC210
// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Score read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseScore(jsonObject);
    }

    // EFFECTS: parses score from JSON object and returns it
    private Score parseScore(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String timeSignature = jsonObject.getString("timeSignature");
        String tone = jsonObject.getString("tonality"); //tonality
        char clef = jsonObject.getString("clef").charAt(0);
        int tempo = jsonObject.getInt("tempo");

        Score sco = new Score(name,tone,timeSignature,clef,tempo);
        addAllNotes(sco,jsonObject);
        return sco;
    }

    // MODIFIES: sco
    // EFFECTS: parse Notes from JSON object and adds them to Score
    private void addAllNotes(Score sco, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("notes");
        for (Object json:jsonArray) {
            JSONObject nextNotes = (JSONObject) json;
            addOneNote(sco,nextNotes);
        }
    }

    // MODIFIES: sco
    // EFFECTS: parse a note from JSON object and adds it to Score
    private void addOneNote(Score sco, JSONObject jsonObject) {
        int octave = jsonObject.getInt("octave");
        int key = jsonObject.getInt("key");
        float beat = jsonObject.getFloat("beat");
        Notes n = new Notes(key,beat,octave);
        sco.addNotes(n);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }
}
