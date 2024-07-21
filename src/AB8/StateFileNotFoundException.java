package AB8;

import java.io.FileNotFoundException;

/**
 * Signals that an attempt to open the state file denoted by a specified pathname has failed.
 */
public class StateFileNotFoundException extends FileNotFoundException {

    public StateFileNotFoundException() {
        super();
    }

    public StateFileNotFoundException(String message) {
        super(message);
    }

    public StateFileNotFoundException(String path, String reason) {
        super(path + ((reason == null)
                ? ""
                : " (" + reason + ")"))
        ;

    }
}
