package com.gorolykmaxim.homemediaapp.qbittorrent;

import com.gorolykmaxim.homemediaapp.common.DownloadSpeed;
import com.gorolykmaxim.homemediaapp.common.DownloadSpeedFormatter;
import com.gorolykmaxim.homemediaapp.common.DurationFormatter;
import com.gorolykmaxim.homemediaapp.common.SizeFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.*;

public class QbittorrentTest {

    private SizeFormatter sizeFormatter;
    private DurationFormatter durationFormatter;
    private DownloadSpeedFormatter downloadSpeedFormatter;
    private NumberFormat percentageFormat;
    private QbittorrentFactory factory;
    private Map<String, String> rawQbittorrent;

    @Before
    public void setUp() throws Exception {
        sizeFormatter = Mockito.mock(SizeFormatter.class);
        durationFormatter = Mockito.mock(DurationFormatter.class);
        downloadSpeedFormatter = Mockito.mock(DownloadSpeedFormatter.class);
        percentageFormat = NumberFormat.getPercentInstance(Locale.US);
        factory = new QbittorrentFactory(sizeFormatter, durationFormatter, downloadSpeedFormatter, percentageFormat);
        rawQbittorrent = new HashMap<>();
        rawQbittorrent.put("hash", UUID.randomUUID().toString());
        rawQbittorrent.put("name", "A torrent");
        rawQbittorrent.put("size", "1234000");
        rawQbittorrent.put("progress", "0.42");
        rawQbittorrent.put("state", "downloading");
        rawQbittorrent.put("dlspeed", "2234000");
        rawQbittorrent.put("eta", "121");
    }

    @Test(expected = QbittorrentFactory.MissingMandatoryFieldsError.class)
    public void mandatoryFieldsAreMissing() {
        factory.create(Collections.emptyMap());
    }

    @Test
    public void create() {
        Qbittorrent qbittorrent = factory.create(rawQbittorrent);
        Assert.assertEquals(rawQbittorrent.get("hash"), qbittorrent.getId());
        Assert.assertEquals(rawQbittorrent.get("name"), qbittorrent.getName());
        Mockito.when(sizeFormatter.format(Long.parseLong(rawQbittorrent.get("size")))).thenReturn("1 MB");
        Assert.assertEquals("1 MB", qbittorrent.getSize());
        Assert.assertFalse(qbittorrent.isComplete());
        Assert.assertEquals("42%", qbittorrent.getProgress());
        Assert.assertEquals(rawQbittorrent.get("state"), qbittorrent.getState());
        DownloadSpeed speed = new DownloadSpeed(Long.parseLong(rawQbittorrent.get("dlspeed")), "second");
        Mockito.when(downloadSpeedFormatter.format(speed)).thenReturn("2 MB / second");
        Assert.assertEquals("2 MB / second", qbittorrent.getDownloadSpeed());
        Mockito.when(durationFormatter.format(Duration.ofSeconds(Long.parseLong(rawQbittorrent.get("eta"))))).thenReturn("0:2:1");
        Assert.assertEquals("0:2:1", qbittorrent.getTimeRemaining());
    }

    @Test
    public void twoQbittorrentsAreSame() {
        Qbittorrent qbittorrent1 = factory.create(rawQbittorrent);
        Qbittorrent qbittorrent2 = factory.create(rawQbittorrent);
        Assert.assertEquals(qbittorrent1, qbittorrent2);
        Assert.assertEquals(qbittorrent1.hashCode(), qbittorrent2.hashCode());
    }

    @Test
    public void twoQbittorrentsAreDifferent() {
        Qbittorrent qbittorrent1 = factory.create(rawQbittorrent);
        rawQbittorrent.put("name", "another torrent");
        Qbittorrent qbittorrent2 = factory.create(rawQbittorrent);
        Assert.assertNotEquals(qbittorrent1, qbittorrent2);
        Assert.assertNotEquals(qbittorrent1.hashCode(), qbittorrent2.hashCode());
    }
}
