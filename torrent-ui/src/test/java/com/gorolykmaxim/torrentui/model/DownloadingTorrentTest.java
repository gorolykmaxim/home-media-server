package com.gorolykmaxim.torrentui.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

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
    public void findById() {
        String id = UUID.randomUUID().toString();
        DownloadingTorrent downloadingTorrent = Mockito.mock(DownloadingTorrent.class);
        List<DownloadingTorrent> downloadingTorrentList = Collections.singletonList(downloadingTorrent);
        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("hashes", id);
        Mockito.when(service.find(expectedParameters)).thenReturn(downloadingTorrentList);
        DownloadingTorrent actualDownloadingTorrent = repository.findById(id);
        Assert.assertEquals(downloadingTorrent, actualDownloadingTorrent);
    }

    @Test(expected = DownloadingTorrentRepository.TorrentDoesNotExistError.class)
    public void failToFindByIdSinceSuchTorrentDoesNotExist() {
        String id = UUID.randomUUID().toString();
        Map<String, String> expectedParameters = new HashMap<>();
        expectedParameters.put("hashes", id);
        Mockito.when(service.find(expectedParameters)).thenReturn(Collections.emptyList());
        repository.findById(id);
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
