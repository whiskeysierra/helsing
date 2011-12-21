package de.bht.pat.tenzing.events;

import java.io.File;

public final class ResultEvent {

    private final File file;

    public ResultEvent(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

}
