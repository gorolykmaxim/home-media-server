package com.gorolykmaxim.homemediaapp.service.api;

import com.gorolykmaxim.homemediaapp.model.torrent.DownloadingTorrent;
import com.gorolykmaxim.homemediaapp.model.torrent.DownloadingTorrentRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

public class DownloadsControllerTest {
    private DownloadingTorrentRepository repository;
    private DownloadsController controller;

    @Before
    public void setUp() throws Exception {
        repository = Mockito.mock(DownloadingTorrentRepository.class);
        controller = new DownloadsController(repository);
    }

    @Test
    public void getAll() {
        List<DownloadingTorrent> expectedDownloads = Collections.singletonList(Mockito.mock(DownloadingTorrent.class));
        Mockito.when(repository.findDownloading()).thenReturn(expectedDownloads);
        List<DownloadingTorrent> downloads = controller.getAll();
        Assert.assertEquals(expectedDownloads, downloads);
    }
}
