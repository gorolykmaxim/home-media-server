package com.gorolykmaxim.thumbnailsearch.contextualwebsearch.cache;

import com.gorolykmaxim.thumbnailsearch.contextualwebsearch.api.Image;
import com.gorolykmaxim.thumbnailsearch.contextualwebsearch.api.ImageList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class ImageCacheTest {

    private CachedImageRepository repository;
    private CachedImageFactory factory;
    private CleanupStrategy cleanupStrategy;
    private ImageCache cache;
    private String searchTerm;

    @Before
    public void setUp() throws Exception {
        repository = Mockito.mock(CachedImageRepository.class);
        factory = Mockito.mock(CachedImageFactory.class);
        cleanupStrategy = Mockito.mock(CleanupStrategy.class);
        cache = new ImageCache(repository, factory, cleanupStrategy);
        searchTerm = "Game of Thrones";
    }

    @Test
    public void dontFindBySearchTermAndIndex() {
        Mockito.when(repository.findAllBySearchTermLike(searchTerm))
                .thenReturn(Collections.emptyList());
        Optional<CachedImage> possibleCachedImage = cache.findBySearchTermAndIndex(searchTerm);
        Assert.assertFalse(possibleCachedImage.isPresent());
    }

    @Test
    public void findBySearchTermAndIndex() {
        CachedImage image = Mockito.mock(CachedImage.class);
        Mockito.when(repository.findAllBySearchTermLike(searchTerm))
                .thenReturn(Collections.singletonList(image));
        Optional<CachedImage> possibleImage = cache.findBySearchTermAndIndex(searchTerm);
        Assert.assertEquals(image, possibleImage.get());
    }

    @Test
    public void save() {
        Image image = new Image();
        image.setImageUrl("http://twitter.com");
        Image imageWithLongUri = new Image();
        imageWithLongUri.setImageUrl("http://url.to/image/with/uri/is/to/long/for/us/to/store");
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