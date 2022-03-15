package twitter.Exceptions;

public class FeedServiceException extends Exception{
    public FeedServiceException() {
        super();
    }

    public FeedServiceException(String message) {
        super(message);
    }

    public FeedServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeedServiceException(Throwable cause) {
        super(cause);
    }

    protected FeedServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
