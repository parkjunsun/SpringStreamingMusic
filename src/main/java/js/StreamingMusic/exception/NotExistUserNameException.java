package js.StreamingMusic.exception;

public class NotExistUserNameException extends RuntimeException{
    public NotExistUserNameException() {
        super();
    }

    public NotExistUserNameException(String message) {
        super(message);
    }

    public NotExistUserNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistUserNameException(Throwable cause) {
        super(cause);
    }
}
