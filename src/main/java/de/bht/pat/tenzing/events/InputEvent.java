package de.bht.pat.tenzing.events;

public final class InputEvent {

    private final String line;

    public InputEvent(String line) {
        this.line = line;
    }

    public String getLine() {
        return line;
    }

}
