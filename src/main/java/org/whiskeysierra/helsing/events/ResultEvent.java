package org.whiskeysierra.helsing.events;

import org.whiskeysierra.helsing.bean.Duration;
import org.whiskeysierra.helsing.sql.SelectStatement;

import java.io.File;

public final class ResultEvent {

    private final Iterable<File> files;
    private final SelectStatement statement;
    private final Duration duration;

    public ResultEvent(Iterable<File> files, SelectStatement statement, Duration duration) {
        this.files = files;
        this.statement = statement;
        this.duration = duration;
    }

    public Iterable<File> getFiles() {
        return files;
    }

    public SelectStatement getStatement() {
        return statement;
    }

    public Duration getDuration() {
        return duration;
    }

}
