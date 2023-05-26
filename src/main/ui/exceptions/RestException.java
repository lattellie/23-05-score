package ui.exceptions;

import java.awt.*;

public class RestException extends Throwable {
    int width;
    int noteHeight;
    float beat;
    Graphics graphics;

    public RestException(int w, int noteHeight, float beat, Graphics g) {
        this.width = w;
        this.noteHeight = noteHeight;
        this.beat = beat;
        this.graphics = g;
    }

    // GETTERS:
    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.noteHeight;
    }

    public float getBeat() {
        return this.beat;
    }

    public Graphics getGraphic() {
        return this.graphics;
    }

}
