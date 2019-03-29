package com.gorolykmaxim.homemediaapp.service.view.tvshow;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

public class ThumbnailPrototypeTest {
    private URI thumbnail;

    @Before
    public void setUp() throws Exception {
        thumbnail = URI.create("http://stock.com/image.jpeg");
    }

    @Test
    public void thumbnail() {
        ThumbnailPrototype prototype = new ThumbnailPrototype();
        prototype.setThumbnail(thumbnail);
        Assert.assertEquals(thumbnail, prototype.getThumbnail());
    }

    @Test
    public void twoPrototypesAreSame() {
        ThumbnailPrototype prototype1 = new ThumbnailPrototype();
        prototype1.setThumbnail(thumbnail);
        ThumbnailPrototype prototype2 = new ThumbnailPrototype();
        prototype2.setThumbnail(thumbnail);
        Assert.assertEquals(prototype1, prototype2);
        Assert.assertEquals(prototype1.hashCode(), prototype2.hashCode());
    }

    @Test
    public void twoPrototypesAreDifferent() {
        ThumbnailPrototype prototype1 = new ThumbnailPrototype();
        prototype1.setThumbnail(thumbnail);
        ThumbnailPrototype prototype2 = new ThumbnailPrototype();
        prototype2.setThumbnail(URI.create("http:/thumblr.com/image.bmp"));
        Assert.assertNotEquals(prototype1, prototype2);
        Assert.assertNotEquals(prototype1.hashCode(), prototype2.hashCode());
    }
}
