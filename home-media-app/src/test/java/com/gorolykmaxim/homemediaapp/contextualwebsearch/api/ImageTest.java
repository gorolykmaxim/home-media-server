package com.gorolykmaxim.homemediaapp.contextualwebsearch.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

public class ImageTest {
    private String expectedUri;

    @Before
    public void setUp() throws Exception {
        expectedUri = "http://localhost";
    }

    @Test
    public void imageUrl() {
        Image image = new Image();
        image.setImageUrl(expectedUri);
        Assert.assertEquals(URI.create(expectedUri), image.getUri());
    }

    @Test
    public void invalidImageUrl() {
        String url = "http://badsite.com/news//article";
        Image image = new Image();
        image.setImageUrl(url);
        Assert.assertEquals(URI.create(url), image.getUri());
    }

    @Test
    public void twoImagesAreSame() {
        Image image1 = new Image();
        image1.setImageUrl(expectedUri);
        Image image2 = new Image();
        image2.setImageUrl(expectedUri);
        Assert.assertEquals(image1, image2);
        Assert.assertEquals(image1.hashCode(), image2.hashCode());
    }

    @Test
    public void twoImagesAreDifferent() {
        Image image1 = new Image();
        image1.setImageUrl(expectedUri);
        Image image2 = new Image();
        image2.setImageUrl("http://192.168.0.1");
        Assert.assertNotEquals(image1, image2);
        Assert.assertNotEquals(image1.hashCode(), image2.hashCode());
    }
}
