package persistence;

import org.json.JSONObject;

// Adapted from "JsonSerializationDemo" by CPSC210
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
