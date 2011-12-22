package de.bht.pat.tenzing.events;

public final class ExecutionError {

    private final Exception exception;

    public ExecutionError(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

}
