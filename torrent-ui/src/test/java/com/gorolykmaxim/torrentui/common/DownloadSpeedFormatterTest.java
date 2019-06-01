package com.gorolykmaxim.torrentui.common;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DownloadSpeedFormatterTest {

    private DownloadSpeedFormatter downloadSpeedFormatter;
    private SizeFormatter sizeFormatter;

    @Before
    public void setUp() throws Exception {
        sizeFormatter = Mockito.mock(SizeFormatter.class);
        downloadSpeedFormatter = new DownloadSpeedFormatter("%s / %s", sizeFormatter);
    }

    @Test
    public void format() {
        DownloadSpeed downloadSpeed = new DownloadSpeed(132000, "second");
        Mockito.when(sizeFormatter.format(downloadSpeed.getSize())).thenReturn("132 KB");
        String speed = downloadSpeedFormatter.format(downloadSpeed);
        Assert.assertEquals("132 KB / second", speed);
    }
}
