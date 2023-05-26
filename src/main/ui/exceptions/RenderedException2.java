package ui.exceptions;

import ui.tools.NotesDrawer3;

public class RenderedException2 extends Exception {
    private int up;
    private NotesDrawer3.NoteProperty np;

    public RenderedException2(int up, NotesDrawer3.NoteProperty np) {
        this.up = up;
        this.np = np;
    }

    public boolean getBooleanUp() {
        return (up == 1);
    }

    public NotesDrawer3.NoteProperty getNoteProperty() {
        return np;
    }
}
