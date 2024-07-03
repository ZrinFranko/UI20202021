package ui;

public class InvalidEntryException extends RuntimeException {

    public InvalidEntryException(String message) {
        super(message);
    }

    public InvalidEntryException() {
        super();
    }

}
