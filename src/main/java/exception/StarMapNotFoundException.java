package exception;

public class StarMapNotFoundException extends Throwable {
    private static final String DEFAULT_MESSAGE = "Star map with this ID does not exist";

    public StarMapNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public StarMapNotFoundException(String message) {
        super(message);
    }
}
