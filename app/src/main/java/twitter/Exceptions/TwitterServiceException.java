package twitter.Exceptions;

public class TwitterServiceException extends Exception{
    public TwitterServiceException() {
        super();
    }

    public TwitterServiceException(String message) {
        super(message);
    }

    public TwitterServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TwitterServiceException(Throwable cause) {
        super(cause);
    }

    protected TwitterServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
