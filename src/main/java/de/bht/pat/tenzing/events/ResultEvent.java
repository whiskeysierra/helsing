package de.bht.pat.tenzing.events;

import java.io.File;
import java.util.concurrent.TimeUnit;

public final class ResultEvent {

    private final File file;
    private final long duration;
    private final TimeUnit durationUnit;

    public ResultEvent(File file, long duration, TimeUnit durationUnit) {
        this.file = file;
        this.duration = duration;
        this.durationUnit = durationUnit;
    }

    public File getFile() {
        return file;
    }

    public long getDuration() {
        return duration;
    }

    public TimeUnit getDurationUnit() {
        return durationUnit;
    }

}
