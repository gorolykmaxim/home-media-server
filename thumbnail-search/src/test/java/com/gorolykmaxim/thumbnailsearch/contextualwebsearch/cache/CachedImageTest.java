package com.gorolykmaxim.thumbnailsearch.contextualwebsearch.cache;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

public class CachedImageTest {
    private CachedImageFactory factory;
    private String uri;
    private String searchTerm;

    @Before
    public void setUp() throws Exception {
        uri = "https://google.com";
        searchTerm = "The Walking Dead";
        factory = new CachedImageFactory();
    }

    @Test
    public void uriIsNotSupported() {
        StringBuilder longUriBuilder = new StringBuilder("http://");
        for (int i = 0; i < 2001; i++) {
            longUriBuilder.append("l");
        }
        Assert.assertFalse(factory.isUriSupported(longUriBuilder.toString()));
    }

    @Test
    public void uriIsSupported() {
        Assert.assertTrue(factory.isUriSupported(uri));
    }

    @Test
    public void create() {
        CachedImage image = factory.create(uri, searchTerm);
        Assert.assertEquals(uri, image.getUri());
        Assert.assertEquals(searchTerm, image.getSearchTerm());
        Assert.assertNotNull(image.getCreationDate());
    }

    @Test
    public void twoImagesAreSimilar() {
        CachedImage image1 = factory.create(uri, searchTerm);
        CachedImage image2 = factory.create(uri, searchTerm);
        Assert.assertEquals(image1, image2);
        Assert.assertEquals(image1.hashCode(), image2.hashCode());
    }

    @Test
    public void twoImagesAreDifferent() {
        CachedImage image1 = factory.create(uri, searchTerm);
        CachedImage image2 = factory.create(uri, "different term");
        Assert.assertNotEquals(image1, image2);
        Assert.assertNotEquals(image1.hashCode(), image2.hashCode());
        image2 = factory.create("http://bing.com", searchTerm);
        Assert.assertNotEquals(image1, image2);
        Assert.assertNotEquals(image1.hashCode(), image2.hashCode());
    }
}