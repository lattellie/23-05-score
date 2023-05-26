package ui.exceptions;

import ui.tools.NotesDrawer3;

public class RenderedException extends Exception {
    private int up;
    private NotesDrawer3.NoteProperty np;

    public RenderedException(int up, NotesDrawer3.NoteProperty np) {
        this.up = up;
        this.np = np;
    }

    // GETTERS
    public int getIntUp() {
        return up;
    }

    public boolean getBooleanUp() {
        return (up == 1);
    }

    public NotesDrawer3.NoteProperty getNoteProperty() {
        return np;
    }
}
