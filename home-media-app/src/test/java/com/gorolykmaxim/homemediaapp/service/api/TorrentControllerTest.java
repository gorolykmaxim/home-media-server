package com.gorolykmaxim.homemediaapp.service.api;

import com.gorolykmaxim.homemediaapp.model.torrent.*;
import com.gorolykmaxim.homemediaapp.service.model.TorrentPrototype;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TorrentControllerTest {
    private TorrentFactory factory;
    private TorrentRepository torrentRepository;
    private DownloadingTorrentRepository downloadingTorrentRepository;
    private TorrentController controller;
    private TorrentPrototype prototype;

    @Before
    public void setUp() throws Exception {
        factory = Mockito.mock(TorrentFactory.class);
        torrentRepository = Mockito.mock(TorrentRepository.class);
        downloadingTorrentRepository = Mockito.mock(DownloadingTorrentRepository.class);
        controller = new TorrentController(factory, torrentRepository, downloadingTorrentRepository);
        prototype = new TorrentPrototype();
        prototype.setMagnetLink("magnet link:");
        prototype.setDownloadFolder("/downloads/");
    }

    @Test
    public void getAll() {
        List<DownloadingTorrent> expectedTorrents = Collections.singletonList(Mockito.mock(DownloadingTorrent.class));
        Mockito.when(downloadingTorrentRepository.findAll()).thenReturn(expectedTorrents);
        List<DownloadingTorrent> torrents = controller.getAll();
        Assert.assertEquals(expectedTorrents, torrents);
    }

    @Test
    public void create() {
        Torrent torrent = Mockito.mock(Torrent.class);
        Mockito.when(factory.createMagnet(prototype.getMagnetLink(), prototype.getDownloadFolder()))
                .thenReturn(torrent);
        controller.create(prototype);
        Mockito.verify(torrentRepository).save(torrent);
    }

    @Test(expected = TorrentController.InvalidTorrentError.class)
    public void failToCreateDueToIncorrectPrototype() {
        Mockito.when(factory.createMagnet(prototype.getMagnetLink(), prototype.getDownloadFolder()))
                .thenThrow(new IllegalArgumentException());
        controller.create(prototype);
    }

    @Test
    public void delete() {
        String id = UUID.randomUUID().toString();
        controller.delete(id);
        Mockito.verify(torrentRepository).deleteById(id);
    }
}
