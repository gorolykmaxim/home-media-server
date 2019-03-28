package com.gorolykmaxim.homemediaapp.contextualwebsearch;

import com.gorolykmaxim.homemediaapp.contextualwebsearch.api.Image;
import com.gorolykmaxim.homemediaapp.contextualwebsearch.api.ImageList;
import com.gorolykmaxim.homemediaapp.contextualwebsearch.api.ImageSearchApi;
import com.gorolykmaxim.homemediaapp.contextualwebsearch.cache.CachedImage;
import com.gorolykmaxim.homemediaapp.contextualwebsearch.cache.ImageCache;
import com.gorolykmaxim.homemediaapp.model.tvshow.Thumbnail;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;

public class ImageSearchTest {
    private ImageCache cache;
    private ImageSearchApi api;
    private ImageSearch search;
    private String searchTerm;
    private int index;

    @Before
    public void setUp() throws Exception {
        cache = Mockito.mock(ImageCache.class);
        api = Mockito.mock(ImageSearchApi.class);
        search = new ImageSearch(cache, api);
        searchTerm = "kittens";
        index = 5;
    }

    @Test
    public void findThumbnailInCache() {
        CachedImage cachedImage = Mockito.mock(CachedImage.class);
        Mockito.when(cache.findBySearchTermAndIndex(searchTerm, index)).thenReturn(Optional.of(cachedImage));
        Optional<Thumbnail> possibleThumbnail = search.findThumbnailBySearchTermAndIndex(searchTerm, index);
        Assert.assertEquals(cachedImage, possibleThumbnail.get());
    }

    @Test
    public void findThumbnailUsingApi() {
        Mockito.when(cache.findBySearchTermAndIndex(searchTerm, index)).thenReturn(Optional.empty());
        ImageList imageList = new ImageList();
        Image image = new Image();
        imageList.setImages(Collections.singletonList(image));
        Mockito.when(api.findImagesBySearchTerm(searchTerm)).thenReturn(imageList);
        Optional<Thumbnail> possibleThumbnail = search.findThumbnailBySearchTermAndIndex(searchTerm, index);
        Assert.assertEquals(image, possibleThumbnail.get());
        Mockito.verify(cache).save(imageList, searchTerm);
    }

    @Test
    public void findNoThumbnailUsingApi() {
        Mockito.when(cache.findBySearchTermAndIndex(searchTerm, index)).thenReturn(Optional.empty());
        ImageList imageList = new ImageList();
        imageList.setImages(Collections.emptyList());
        Mockito.when(api.findImagesBySearchTerm(searchTerm)).thenReturn(imageList);
        Optional<Thumbnail> possibleThumbnail = search.findThumbnailBySearchTermAndIndex(searchTerm, index);
        Assert.assertFalse(possibleThumbnail.isPresent());
        Mockito.verify(cache, Mockito.never()).save(Mockito.any(), Mockito.anyString());
    }
}
