package exception;

public class OrderNotFoundException extends Throwable {
    private static final String DEFAULT_MESSAGE = "Order with this ID does not exist";

    public OrderNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
