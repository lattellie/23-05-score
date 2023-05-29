package ui.exceptions;

public class RestException extends Throwable {

    private int curBar;
    public RestException(int curBar) {
        this.curBar = curBar;
    }

}
