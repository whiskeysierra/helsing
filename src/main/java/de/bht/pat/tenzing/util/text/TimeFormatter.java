package de.bht.pat.tenzing.util.text;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.TimeUnit;

public final class TimeFormatter {

    private final List<TimeUnit> units = ImmutableList.of(
        TimeUnit.MINUTES, TimeUnit.SECONDS
    );

    public String format(long duration, TimeUnit durationUnit) {
        final List<String> times = Lists.newLinkedList();

        long time = duration;
        for (TimeUnit unit : units) {
            final long scaled = unit.convert(time, durationUnit);
            if (scaled > 0) {
                time -= durationUnit.convert(scaled, unit);
                times.add(scaled + " " + localize(unit));
            }
        }

        if (times.isEmpty()) {
            return "0.00 " + localize(Lists.reverse(units).get(0));
        } else {
            return Joiner.on(", ").join(times);
        }
    }

    private String localize(TimeUnit unit) {
        switch (unit) {
            case SECONDS:
                return "sec";
            case MINUTES:
                return "min";
            default:
                throw new UnsupportedOperationException(unit.name());
        }
    }

}
