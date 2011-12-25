package org.whiskeysierra.helsing.events;

public final class PrintLineEvent {

    private final String line;

    public PrintLineEvent() {
        this("");
    }

    public PrintLineEvent(String line) {
        this.line = line;
    }

    public String getLine() {
        return line;
    }

}
