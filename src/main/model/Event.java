package model;

import java.util.Calendar;
import java.util.Date;

// adapted from: cpsc210.AlarmSystem
public class Event {
    private static final int HASH_CONSTANT = 13;
    private Date dateLogged;
    private String description;

    public Event(String description) {
        dateLogged = Calendar.getInstance().getTime();
        this.description = description;
    }

    // EFFECTS: return the date
    public Date getDate() {
        return dateLogged;
    }

    // EFFECTS: return the description
    public String getDescription() {
        return description;
    }

    // EFFECTS: override the equal
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Event otherEvent = (Event) other;
        return (this.dateLogged.equals(otherEvent.dateLogged) && this.description.equals(otherEvent.description));
    }

    // EFFECTS: override the hashcode
    @Override
    public int hashCode() {
        return (HASH_CONSTANT * dateLogged.hashCode() + description.hashCode());
    }

    // EFFECTS: override the toString methon
    @Override
    public String toString() {
        return dateLogged.toString() + "\n" + description;
    }
}
