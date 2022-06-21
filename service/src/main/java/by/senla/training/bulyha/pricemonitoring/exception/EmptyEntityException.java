package by.senla.training.bulyha.pricemonitoring.exception;

public class EmptyEntityException extends RuntimeException {

    public EmptyEntityException() {
    }

    public EmptyEntityException(String message) {
        super(message);
    }

    public EmptyEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyEntityException(Throwable cause) {
        super(cause);
    }

    public EmptyEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
