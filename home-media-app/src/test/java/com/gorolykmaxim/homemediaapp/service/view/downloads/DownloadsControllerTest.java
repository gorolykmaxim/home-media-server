package com.gorolykmaxim.homemediaapp.service.view.downloads;

import com.gorolykmaxim.homemediaapp.model.torrent.DownloadingTorrentRepository;
import com.gorolykmaxim.homemediaapp.service.view.ViewError;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Map;

public class DownloadsControllerTest {
    private DownloadingTorrentRepository repository;
    private DownloadsController controller;

    @Before
    public void setUp() throws Exception {
        repository = Mockito.mock(DownloadingTorrentRepository.class);
        controller = new DownloadsController(repository);
    }

    @Test
    public void showDownloads() {
        Mockito.when(repository.findDownloading()).thenReturn(Collections.emptyList());
        ModelAndView modelAndView = controller.showDownloads();
        Assert.assertEquals("downloads", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        Assert.assertEquals(Collections.emptyList(), model.get("downloads"));
    }

    @Test(expected = ViewError.class)
    public void failShowDownloads() {
        Mockito.when(repository.findDownloading()).thenThrow(Mockito.mock(RuntimeException.class));
        controller.showDownloads();
    }
}
