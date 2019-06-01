package com.gorolykmaxim.torrentui.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadingTorrentTest {
    private TorrentService service;
    private DownloadingTorrentRepository repository;
    private String sort;
    private boolean reverse;

    @Before
    public void setUp() throws Exception {
        service = Mockito.mock(TorrentService.class);
        sort = "progress";
        reverse = true;
        repository = new DownloadingTorrentRepository(service, sort, reverse);
    }

    @Test
    public void findAll() {
        List<DownloadingTorrent> downloadingTorrentList = Collections.singletonList(Mockito.mock(DownloadingTorrent.class));
        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("sort", sort);
        expectedParameters.put("reverse", Boolean.toString(reverse));
        Mockito.when(service.find(expectedParameters)).thenReturn(downloadingTorrentList);
        List<DownloadingTorrent> actualDownloadingTorrentList = repository.findAll();
        Assert.assertEquals(downloadingTorrentList, actualDownloadingTorrentList);
    }

    @Test
    public void findDownloading() {
        List<DownloadingTorrent> downloadingTorrentList = Collections.singletonList(Mockito.mock(DownloadingTorrent.class));
        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("sort", sort);
        expectedParameters.put("filter", "downloading");
        expectedParameters.put("reverse", Boolean.toString(reverse));
        Mockito.when(service.find(expectedParameters)).thenReturn(downloadingTorrentList);
        List<DownloadingTorrent> actualDownloadingTorrentList = repository.findDownloading();
        Assert.assertEquals(downloadingTorrentList, actualDownloadingTorrentList);
    }
}
