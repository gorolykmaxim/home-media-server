package com.gorolykmaxim.homemediaapp.contextualwebsearch.cache;

import com.gorolykmaxim.homemediaapp.contextualwebsearch.api.Image;
import com.gorolykmaxim.homemediaapp.contextualwebsearch.api.ImageList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class ImageCacheTest {

    private CachedImageRepository repository;
    private CachedImageFactory factory;
    private CleanupStrategy cleanupStrategy;
    private ImageCache cache;
    private String searchTerm;
    private int index;

    @Before
    public void setUp() throws Exception {
        repository = Mockito.mock(CachedImageRepository.class);
        factory = Mockito.mock(CachedImageFactory.class);
        cleanupStrategy = Mockito.mock(CleanupStrategy.class);
        cache = new ImageCache(repository, factory, cleanupStrategy);
        searchTerm = "Game of Thrones";
        index = 0;
    }

    @Test
    public void dontFindBySearchTermAndIndex() {
        Mockito.when(repository.findAllBySearchTermLike(searchTerm, PageRequest.of(index, 1)))
                .thenReturn(Collections.emptyList());
        Optional<CachedImage> possibleCachedImage = cache.findBySearchTermAndIndex(searchTerm, index);
        Assert.assertFalse(possibleCachedImage.isPresent());
    }

    @Test
    public void findBySearchTermAndIndex() {
        CachedImage image = Mockito.mock(CachedImage.class);
        Mockito.when(repository.findAllBySearchTermLike(searchTerm, PageRequest.of(index, 1)))
                .thenReturn(Collections.singletonList(image));
        Optional<CachedImage> possibleImage = cache.findBySearchTermAndIndex(searchTerm, index);
        Assert.assertEquals(image, possibleImage.get());
    }

    @Test
    public void save() {
        Image image = new Image();
        image.setImageUrl(URI.create("http://twitter.com"));
        Image imageWithLongUri = new Image();
        imageWithLongUri.setImageUrl(URI.create("http://url.to/image/with/uri/is/to/long/for/us/to/store"));
        ImageList imageList = new ImageList();
        imageList.setImages(Arrays.asList(image, imageWithLongUri));
        Mockito.when(factory.isUriSupported(imageWithLongUri.getUri())).thenReturn(false);
        Mockito.when(factory.isUriSupported(image.getUri())).thenReturn(true);
        CachedImage cachedImage = Mockito.mock(CachedImage.class);
        Mockito.when(factory.create(image.getUri(), searchTerm)).thenReturn(cachedImage);
        // Mocking done.
        cache.save(imageList, searchTerm);
        Mockito.verify(repository).save(cachedImage);
        Mockito.verifyNoMoreInteractions(repository);
        Mockito.verify(cleanupStrategy).clean(repository);
    }
}
