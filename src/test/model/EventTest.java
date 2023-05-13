package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

// reference: cpsc210.AlarmSystem
public class EventTest {
    private Event e;
    private Date d;

    @BeforeEach
    public void runBefore() {
        e = new Event("Notes added");
        d = Calendar.getInstance().getTime();
    }

    @Test
    public void testEvent() {
        assertEquals("Notes added", e.getDescription());
        assertTrue(Math.abs(d.getTime() - e.getDate().getTime()) < 100);
    }

    @Test
    public void testToString() {
        assertEquals(d.toString() + "\n" + "Notes added", e.toString());
    }

    @Test
    public void testEqualandHash() {
        Event e1 = new Event("Notes added");
        Event e2 = new Event("Notes added");
        Event e3 = null;
        Score s = new Score();
        boolean b = e1.equals(e2);
        boolean b2 = e1.equals(e3);
        boolean b3 = e1.equals(s);
        assertEquals(b3, false);
        assertEquals(b, true);
        assertEquals(b2, false);
        assertFalse(e1.equals(e3));
        assertFalse(e1.equals(s));
        assertTrue(e1.equals(e2));
        assertEquals(e1.hashCode(), e2.hashCode());
    }
}
