package de.bht.pat.tenzing.events;

import de.bht.pat.tenzing.bean.Duration;

import java.io.File;
import java.util.concurrent.TimeUnit;

public final class ResultEvent {

    private final Iterable<File> files;
    private final Duration duration;

    public ResultEvent(Iterable<File> files, Duration duration) {
        this.files = files;
        this.duration = duration;
    }

    public Iterable<File> getFiles() {
        return files;
    }

    public Duration getDuration() {
        return duration;
    }

}
