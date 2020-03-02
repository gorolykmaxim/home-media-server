package com.gorolykmaxim.torrentui.view;

import com.gorolykmaxim.torrentui.model.*;
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
        long refreshInterval = 5000;
        controller.setRefreshInterval(refreshInterval);
        ModelAndView modelAndView = controller.showTorrentList();
        Assert.assertEquals("list", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        Assert.assertEquals(refreshInterval, model.get("refreshInterval"));
        Assert.assertEquals("/download", model.get("downloadTorrentFormUrl"));
    }

    @Test
    public void renderTorrentList() {
        List<DownloadingTorrent> torrentList = Collections.emptyList();
        Mockito.when(downloadingTorrentRepository.findAll()).thenReturn(torrentList);
        ModelAndView modelAndView = controller.renderTorrentList();
        Assert.assertEquals("torrent-list", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        Assert.assertEquals(torrentList, model.get("torrents"));
        Assert.assertEquals("/delete", model.get("deleteUrlPrefix"));
    }

    @Test()
    public void failToRenderTorrentList() {
        RuntimeException expectedError = new RuntimeException("Error");
        ViewError expectedViewError = new ViewError(expectedError);
        Mockito.when(downloadingTorrentRepository.findAll()).thenThrow(expectedError);
        ModelAndView modelAndView = controller.renderTorrentList();
        Assert.assertEquals("error-component", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        Assert.assertEquals(expectedViewError.getMessage(), model.get("error"));
        Assert.assertNotNull(model.get("stackTrace"));
    }

    @Test
    public void showDownloadTorrentForm() {
        ModelAndView modelAndView = controller.showDownloadTorrentForm();
        Assert.assertEquals("new", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        Assert.assertEquals(defaultDownloadingFolder, model.get("defaultDownloadFolder"));
        Assert.assertEquals("/download", model.get("submitUrl"));
    }

    @Test
    public void showDeleteTorrentPrompt() {
        String id = UUID.randomUUID().toString();
        DownloadingTorrent torrent = Mockito.mock(DownloadingTorrent.class);
        Mockito.when(downloadingTorrentRepository.findById(id)).thenReturn(torrent);
        ModelAndView modelAndView = controller.showDeleteTorrentPrompt(id);
        Assert.assertEquals("delete", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        Assert.assertEquals(torrent, model.get("torrent"));
        Assert.assertEquals(String.format("/delete", id), model.get("deleteUrl"));
        Assert.assertEquals("/", model.get("cancelUrl"));
    }

    @Test(expected = ViewError.class)
    public void failToShowDeleteTorrentPrompt() {
        String id = UUID.randomUUID().toString();
        Mockito.when(downloadingTorrentRepository.findById(id)).thenThrow(Mockito.mock(DownloadingTorrentRepository.TorrentDoesNotExistError.class));
        controller.showDeleteTorrentPrompt(id);
    }

    @Test
    public void deleteTorrentById() {
        DeleteTorrent deleteTorrent = new DeleteTorrent();
        deleteTorrent.setId(UUID.randomUUID().toString());
        String viewName = controller.deleteTorrentById(deleteTorrent);
        Assert.assertEquals("redirect:/", viewName);
        Mockito.verify(torrentRepository).deleteById(deleteTorrent.getId(), deleteTorrent.isDeleteData());
    }

    @Test
    public void deleteTorrentWithContentsById() {
        DeleteTorrent deleteTorrent = new DeleteTorrent();
        deleteTorrent.setId(UUID.randomUUID().toString());
        deleteTorrent.setDeleteData(true);
        String viewName = controller.deleteTorrentById(deleteTorrent);
        Assert.assertEquals("redirect:/", viewName);
        Mockito.verify(torrentRepository).deleteById(deleteTorrent.getId(), deleteTorrent.isDeleteData());
    }

    @Test(expected = ViewError.class)
    public void failToDeleteTorrentById() {
        DeleteTorrent deleteTorrent = new DeleteTorrent();
        deleteTorrent.setId(UUID.randomUUID().toString());
        Mockito.doThrow(Mockito.mock(RuntimeException.class)).when(torrentRepository).deleteById(deleteTorrent.getId(), deleteTorrent.isDeleteData());
        controller.deleteTorrentById(deleteTorrent);
    }

    @Test
    public void downloadTorrent() {
        TorrentPrototype prototype = new TorrentPrototype();
        prototype.setMagnetLink("magnet uri");
        prototype.setDownloadFolder(defaultDownloadingFolder);
        Torrent torrent = Mockito.mock(Torrent.class);
        Mockito.when(factory.createMagnet(prototype.getMagnetLink(), prototype.getDownloadFolder())).thenReturn(torrent);
        String viewName = controller.downloadTorrent(prototype);
        Assert.assertEquals("redirect:/", viewName);
        Mockito.verify(torrentRepository).save(torrent);
    }

    @Test(expected = ViewError.class)
    public void failToDownloadTorrent() {
        TorrentPrototype prototype = new TorrentPrototype();
        prototype.setMagnetLink("magnet uri");
        prototype.setDownloadFolder(defaultDownloadingFolder);
        Torrent torrent = Mockito.mock(Torrent.class);
        Mockito.when(factory.createMagnet(prototype.getMagnetLink(), prototype.getDownloadFolder())).thenReturn(torrent);
        Mockito.doThrow(Mockito.mock(RuntimeException.class)).when(torrentRepository).save(torrent);
        controller.downloadTorrent(prototype);
    }
}
