package com.gorolykmaxim.homemediaapp.model.torrent;

import com.gorolykmaxim.homemediaapp.model.torrent.command.Torrent;
import com.gorolykmaxim.homemediaapp.model.torrent.command.TorrentFactory;
import com.gorolykmaxim.homemediaapp.model.torrent.command.TorrentRepository;
import com.gorolykmaxim.homemediaapp.model.torrent.command.TorrentService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

public class TorrentTest {

    private TorrentFactory factory;
    private TorrentService service;
    private TorrentRepository repository;
    private String downloadFolder;

    @Before
    public void setUp() throws Exception {
        factory = new TorrentFactory();
        service = Mockito.mock(TorrentService.class);
        downloadFolder = "/downloads/";
        repository = new TorrentRepository(service);
    }

    @Test
    public void twoMagnetTorrentsAreSame() {
        Torrent torrent1 = factory.createMagnet("magnet1", "/downloads/");
        Torrent torrent2 = factory.createMagnet("magnet1", "/downloads/");
        Assert.assertEquals(torrent1, torrent2);
        Assert.assertEquals(torrent1.hashCode(), torrent2.hashCode());
    }

    @Test
    public void twoMagnetTorrentsAreDifferent() {
        Torrent torrent1 = factory.createMagnet("magnet1", "/downloads/");
        Torrent torrent2 = factory.createMagnet("magnet2", "/downloads/");
        Assert.assertNotEquals(torrent1, torrent2);
        Assert.assertNotEquals(torrent1.hashCode(), torrent2.hashCode());
        torrent2 = factory.createMagnet("magnet1", "/downloads/media");
        Assert.assertNotEquals(torrent1, torrent2);
        Assert.assertNotEquals(torrent1.hashCode(), torrent2.hashCode());
    }

    @Test
    public void downloadTorrentWithMagnetLink() {
        String magnetUri = "magnet uri";
        Torrent torrent = factory.createMagnet(magnetUri, downloadFolder);
        repository.save(torrent);
        Mockito.verify(service).downloadViaMagnetLink(magnetUri, downloadFolder);
    }

    @Test
    public void deleteTorrentById() {
        String id = UUID.randomUUID().toString();
        repository.deleteById(id);
        Mockito.verify(service).deleteTorrentById(id);
    }
}
