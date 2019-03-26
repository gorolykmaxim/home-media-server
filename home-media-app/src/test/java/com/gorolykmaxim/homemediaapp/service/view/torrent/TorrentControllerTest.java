package com.gorolykmaxim.homemediaapp.service.view.torrent;

import com.gorolykmaxim.homemediaapp.model.torrent.command.Torrent;
import com.gorolykmaxim.homemediaapp.model.torrent.command.TorrentFactory;
import com.gorolykmaxim.homemediaapp.model.torrent.command.TorrentRepository;
import com.gorolykmaxim.homemediaapp.model.torrent.query.DownloadingTorrent;
import com.gorolykmaxim.homemediaapp.model.torrent.query.DownloadingTorrentRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TorrentControllerTest {

    private String defaultDownloadingFolder;
    private TorrentFactory factory;
    private TorrentRepository torrentRepository;
    private DownloadingTorrentRepository downloadingTorrentRepository;
    private TorrentController controller;

    @Before
    public void setUp() throws Exception {
        defaultDownloadingFolder = "/torrents/";
        factory = Mockito.mock(TorrentFactory.class);
        torrentRepository = Mockito.mock(TorrentRepository.class);
        downloadingTorrentRepository = Mockito.mock(DownloadingTorrentRepository.class);
        controller = new TorrentController(factory, torrentRepository, downloadingTorrentRepository);
        controller.setDefaultDownloadFolder(defaultDownloadingFolder);
    }

    @Test
    public void showTorrentList() {
        List<DownloadingTorrent> torrentList = Collections.emptyList();
        Mockito.when(downloadingTorrentRepository.findAll()).thenReturn(torrentList);
        ModelAndView modelAndView = controller.showTorrentList();
        Assert.assertEquals("torrent/list", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        Assert.assertEquals(torrentList, model.get("torrentList"));
    }

    @Test
    public void showDownloadTorrentForm() {
        ModelAndView modelAndView = controller.showDownloadTorrentForm();
        Assert.assertEquals("torrent/new", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        Assert.assertEquals(defaultDownloadingFolder, model.get("defaultDownloadFolder"));
    }

    @Test
    public void deleteTorrentById() {
        String id = UUID.randomUUID().toString();
        String viewName = controller.deleteTorrentById(id);
        Assert.assertEquals("redirect:/torrent", viewName);
        Mockito.verify(torrentRepository).deleteById(id);
    }

    @Test
    public void downloadTorrent() {
        TorrentPrototype prototype = new TorrentPrototype();
        prototype.setMagnetLink("magnet uri");
        prototype.setDownloadFolder(defaultDownloadingFolder);
        Torrent torrent = Mockito.mock(Torrent.class);
        Mockito.when(factory.createMagnet(prototype.getMagnetLink(), prototype.getDownloadFolder())).thenReturn(torrent);
        String viewName = controller.downloadTorrent(prototype);
        Assert.assertEquals("redirect:/torrent", viewName);
        Mockito.verify(torrentRepository).save(torrent);
    }
}
