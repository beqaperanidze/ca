package prjnightsky.exception;

public class UserNotFoundException extends Throwable {
    private static final String DEFAULT_MESSAGE = "User with this ID does not exist";

    public UserNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
