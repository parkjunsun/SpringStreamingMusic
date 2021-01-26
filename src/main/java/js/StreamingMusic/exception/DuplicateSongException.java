package js.StreamingMusic.exception;

public class DuplicateSongException extends RuntimeException{

    public DuplicateSongException() {
        super();
    }

    public DuplicateSongException(String message) {
        super(message);
    }

    public DuplicateSongException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateSongException(Throwable cause) {
        super(cause);
    }
}
