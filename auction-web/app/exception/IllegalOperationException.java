package exception;

public class IllegalOperationException extends Exception {

    public IllegalOperationException(String message) {
        super(message);
    }

    public IllegalOperationException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
