package by.balashevich.periodicals.exception;

public class PublicationProjectException extends Exception{
    public PublicationProjectException() {
        super();
    }

    public PublicationProjectException(String message) {
        super(message);
    }

    public PublicationProjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public PublicationProjectException(Throwable cause) {
        super(cause);
    }
}
