package de.bht.pat.tenzing.util.text;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.bht.pat.tenzing.bean.Duration;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public final class TimeFormatter {

    private final ResourceBundle bundle;
    private final List<TimeUnit> units = Arrays.asList(TimeUnit.values());
    private final List<TimeUnit> reverse = Lists.reverse(units);
    private final Joiner joiner = Joiner.on(", ");

    @Inject
    public TimeFormatter(@Named("unit") ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String format(Duration duration) {
        return format(duration.getValue(), duration.getUnit());
    }

    public String format(long duration, TimeUnit durationUnit) {
        final List<String> times = Lists.newLinkedList();

        long rest = duration;

        for (TimeUnit unit : reverse) {
            final long time = unit.convert(rest, durationUnit);
            if (time > 0) {
                rest -= durationUnit.convert(time, unit);
                times.add(toString(time, unit));
            }
        }

        if (times.isEmpty()) {
            return toString(duration, units.get(0));
        } else {
            return joiner.join(times);
        }
    }

    private String toString(long time, TimeUnit unit) {
        return time + " " + bundle.getString(TimeUnit.class.getName() + "." + unit.name());
    }
}
