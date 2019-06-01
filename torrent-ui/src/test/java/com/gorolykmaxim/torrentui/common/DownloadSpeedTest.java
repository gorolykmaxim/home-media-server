package com.gorolykmaxim.torrentui.common;

import org.junit.Assert;
import org.junit.Test;

public class DownloadSpeedTest {

    @Test
    public void create() {
        DownloadSpeed downloadSpeed = new DownloadSpeed(155, "second");
        Assert.assertEquals(155, downloadSpeed.getSize());
        Assert.assertEquals("second", downloadSpeed.getPeriod());
    }

    @Test
    public void twoDownloadSpeedsAreSame() {
        DownloadSpeed downloadSpeed1 = new DownloadSpeed(3000000, "second");
        DownloadSpeed downloadSpeed2 = new DownloadSpeed(3000000, "second");
        Assert.assertEquals(downloadSpeed1, downloadSpeed2);
        Assert.assertEquals(downloadSpeed1.hashCode(), downloadSpeed2.hashCode());
    }

    @Test
    public void twoDownloadSpeedsAreDifferent() {
        DownloadSpeed downloadSpeed1 = new DownloadSpeed(3000000, "second");
        DownloadSpeed downloadSpeed2 = new DownloadSpeed(1500000, "second");
        Assert.assertNotEquals(downloadSpeed1, downloadSpeed2);
        Assert.assertNotEquals(downloadSpeed1.hashCode(), downloadSpeed2.hashCode());
        downloadSpeed2 = new DownloadSpeed(3000000, "minute");
        Assert.assertNotEquals(downloadSpeed1, downloadSpeed2);
        Assert.assertNotEquals(downloadSpeed1.hashCode(), downloadSpeed2.hashCode());
    }
}
