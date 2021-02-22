package js.StreamingMusic.exception;

public class BoardInputEmptyException extends RuntimeException{

    public BoardInputEmptyException() {
        super();
    }

    public BoardInputEmptyException(String message) {
        super(message);
    }

    public BoardInputEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoardInputEmptyException(Throwable cause) {
        super(cause);
    }
}
