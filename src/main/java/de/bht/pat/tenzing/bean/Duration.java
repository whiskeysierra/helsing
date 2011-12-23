package de.bht.pat.tenzing.bean;

import java.util.concurrent.TimeUnit;

public final class Duration {

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
