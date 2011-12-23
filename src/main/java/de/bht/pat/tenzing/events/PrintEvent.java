package de.bht.pat.tenzing.events;

public final class PrintEvent {

    private final String line;

    public PrintEvent(String line) {
        this.line = line;
    }

    public String getLine() {
        return line;
    }

}
