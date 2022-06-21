package by.senla.training.bulyha.pricemonitoring.exception;

public class CsvException extends RuntimeException {

    public CsvException() {
    }

    public CsvException(String message) {
        super(message);
    }

    public CsvException(String message, Throwable cause) {
        super(message, cause);
    }

    public CsvException(Throwable cause) {
        super(cause);
    }

    public CsvException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
