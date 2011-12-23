package de.bht.pat.tenzing.events;

import de.bht.pat.tenzing.bean.Duration;
import de.bht.pat.tenzing.sql.SelectStatement;

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
