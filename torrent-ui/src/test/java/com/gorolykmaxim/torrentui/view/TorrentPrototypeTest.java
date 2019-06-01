package com.gorolykmaxim.torrentui.view;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TorrentPrototypeTest {

    private String magnet, downloadFolder;

    @Before
    public void setUp() throws Exception {
        magnet = "magnet uri";
        downloadFolder = "/home/user/downloads";
    }

    @Test
    public void magnetLink() {
        TorrentPrototype prototype = new TorrentPrototype();
        prototype.setMagnetLink(magnet);
        Assert.assertEquals(magnet, prototype.getMagnetLink());
    }

    @Test
    public void downloadFolder() {
        TorrentPrototype prototype = new TorrentPrototype();
        prototype.setDownloadFolder(downloadFolder);
        Assert.assertEquals(downloadFolder, prototype.getDownloadFolder());
    }

    @Test
    public void twoPrototypesAreSame() {
        TorrentPrototype prototype1 = new TorrentPrototype();
        prototype1.setMagnetLink(magnet);
        prototype1.setDownloadFolder(downloadFolder);
        TorrentPrototype prototype2 = new TorrentPrototype();
        prototype2.setMagnetLink(magnet);
        prototype2.setDownloadFolder(downloadFolder);
        Assert.assertEquals(prototype1, prototype2);
        Assert.assertEquals(prototype1.hashCode(), prototype2.hashCode());
    }

    @Test
    public void twoPrototypesAreDifferent() {
        TorrentPrototype prototype1 = new TorrentPrototype();
        prototype1.setMagnetLink(magnet);
        prototype1.setDownloadFolder(downloadFolder);
        TorrentPrototype prototype2 = new TorrentPrototype();
        prototype2.setMagnetLink(magnet);
        prototype2.setDownloadFolder("/downloads");
        Assert.assertNotEquals(prototype1, prototype2);
        Assert.assertNotEquals(prototype1.hashCode(), prototype2.hashCode());
        prototype2.setMagnetLink("different magnet uri");
        prototype2.setDownloadFolder(downloadFolder);
        Assert.assertNotEquals(prototype1, prototype2);
        Assert.assertNotEquals(prototype1.hashCode(), prototype2.hashCode());
    }
}
