package org.whiskeysierra.helsing.events;

import org.whiskeysierra.helsing.api.sql.SelectStatement;

import java.io.File;
import java.util.concurrent.TimeUnit;

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

    public static final class Duration {

        private final long value;
        private final TimeUnit unit;

        public Duration(long value, TimeUnit unit) {
            this.value = value;
            this.unit = unit;
        }

        public long getValue() {
            return value;
        }

        public TimeUnit getUnit() {
            return unit;
        }
    }

}
