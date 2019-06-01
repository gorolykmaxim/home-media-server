package com.gorolykmaxim.torrentui.common;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

public class DurationFormatterTest {

    private Duration duration;

    @Before
    public void setUp() throws Exception {
        duration = Duration.ofHours(5).plus(Duration.ofSeconds(15));
    }

    @Test
    public void format() {
        DurationFormatter formatter = new DurationFormatter("H:mm:ss", false);
        String formattedDuration = formatter.format(duration);
        Assert.assertEquals("5:0:15", formattedDuration);
    }

    @Test
    public void formatWithZeros() {
        DurationFormatter formatter = new DurationFormatter("H:mm:ss", true);
        String formattedDuration = formatter.format(duration);
        Assert.assertEquals("5:00:15", formattedDuration);
    }
}
