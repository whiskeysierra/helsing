package org.whiskeysierra.helsing.events;

public final class PrintEvent {

    private final String line;

    public PrintEvent(String line) {
        this.line = line;
    }

    public String getLine() {
        return line;
    }

}
