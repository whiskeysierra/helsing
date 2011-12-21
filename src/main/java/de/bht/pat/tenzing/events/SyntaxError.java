package de.bht.pat.tenzing.events;

public final class SyntaxError {

    private final Exception exception;

    public SyntaxError(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

}
