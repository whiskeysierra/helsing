package de.bht.pat.tenzing.util.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public final class TimeFormatterTest {

    private final TimeFormatter unit = new TimeFormatter();

    @Test
    public void lessThanOneSecond() {
         Assert.assertEquals("0.00 sec", unit.format(100, TimeUnit.MILLISECONDS));
    }

    @Test
    public void seconds() {
        Assert.assertEquals("1 min, 20 sec", unit.format(80, TimeUnit.SECONDS));
    }

    @Test
    public void minutes() {
        Assert.assertEquals("23 min, 10 sec", unit.format(1390, TimeUnit.SECONDS));
    }

}
