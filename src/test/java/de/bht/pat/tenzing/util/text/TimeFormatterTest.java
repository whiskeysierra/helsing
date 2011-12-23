package de.bht.pat.tenzing.util.text;

import com.google.inject.Guice;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public final class TimeFormatterTest {

    private final TimeFormatter unit = Guice.createInjector(new TextModule()).getInstance(TimeFormatter.class);

    @Test
    public void zero() {
        Assert.assertEquals("0 ns", unit.format(0, TimeUnit.DAYS));
    }

    @Test
    public void lessThanOneSecond() {
         Assert.assertEquals("100 ms", unit.format(100, TimeUnit.MILLISECONDS));
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
