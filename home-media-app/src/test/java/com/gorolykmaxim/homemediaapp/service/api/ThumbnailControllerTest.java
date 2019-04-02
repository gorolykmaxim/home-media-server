package com.gorolykmaxim.homemediaapp.service.api;

import com.gorolykmaxim.homemediaapp.model.tvshow.Thumbnail;
import com.gorolykmaxim.homemediaapp.model.tvshow.ThumbnailRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

public class ThumbnailControllerTest {
    private ThumbnailRepository repository;
    private ThumbnailController controller;

    @Before
    public void setUp() throws Exception {
        repository = Mockito.mock(ThumbnailRepository.class);
        controller = new ThumbnailController(repository);
    }

    @Test
    public void findBySearchTerm() {
        String searchTerm = "Friends";
        List<Thumbnail> thumbnailList = Collections.singletonList(Mockito.mock(Thumbnail.class));
        Mockito.when(repository.findThumbnailsBySearchTerm(searchTerm)).thenReturn(thumbnailList);
        Assert.assertEquals(thumbnailList, controller.findBySearchTerm(searchTerm));
    }
}
