package br.com.builders.cliente.exception;

public class ClienteRuntimeException extends RuntimeException {

    public ClienteRuntimeException() {
        super();
    }

    public ClienteRuntimeException(String message) {
        super(message);
    }

    public ClienteRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClienteRuntimeException(Throwable cause) {
        super(cause);
    }

    protected ClienteRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
