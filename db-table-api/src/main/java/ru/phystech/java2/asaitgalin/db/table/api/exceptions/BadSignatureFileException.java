package ru.phystech.java2.asaitgalin.db.table.api.exceptions;

public class BadSignatureFileException extends RuntimeException {

    public BadSignatureFileException() {
        super();
    }

    public BadSignatureFileException(String message) {
        super(message);
    }

    public BadSignatureFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadSignatureFileException(Throwable cause) {
        super(cause);
    }
}
