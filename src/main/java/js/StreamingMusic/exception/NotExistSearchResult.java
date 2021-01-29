package js.StreamingMusic.exception;

public class NotExistSearchResult extends RuntimeException {

    public NotExistSearchResult() {
        super();
    }

    public NotExistSearchResult(String message) {
        super(message);
    }

    public NotExistSearchResult(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistSearchResult(Throwable cause) {
        super(cause);
    }

}
