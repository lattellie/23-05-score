package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

// adapted from: cpsc210.AlarmSystem
public class EventLog implements Iterable<Event> {
    private static EventLog theLog;
    private Collection<Event> events;

    private EventLog() {
        events = new ArrayList<Event>();
    }

    // EFFECTS: return the only EventLog object instance
    public static EventLog getInstance() {
        if (theLog == null) {
            theLog = new EventLog();
        }
        return theLog;
    }

    // MODIFIES: events
    // EFFECTS: add even to the arraylist
    public void logEvent(Event e) {
        events.add(e);
    }


    // MODIFIES: events
    // EFFECTS: clear the eventlog
    public void clear() {
        events.clear();
        logEvent(new Event("Event log cleared."));
    }

    // EFFECTS: return the iterable list of events
    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }
}
