package com.gorolykmaxim.homemediaapp.service.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EpisodePrototypeTest {
    private String magnetUri;

    @Before
    public void setUp() throws Exception {
        magnetUri = "magnet uri:";
    }

    @Test
    public void magnetUri() {
        EpisodePrototype prototype = new EpisodePrototype();
        prototype.setMagnetUri(magnetUri);
        Assert.assertEquals(magnetUri, prototype.getMagnetUri());
    }

    @Test
    public void twoPrototypesAreSame() {
        EpisodePrototype prototype1 = new EpisodePrototype();
        prototype1.setMagnetUri(magnetUri);
        EpisodePrototype prototype2 = new EpisodePrototype();
        prototype2.setMagnetUri(magnetUri);
        Assert.assertEquals(prototype1, prototype2);
        Assert.assertEquals(prototype1.hashCode(), prototype2.hashCode());
    }

    @Test
    public void twoPrototypesAreDifferent() {
        EpisodePrototype prototype1 = new EpisodePrototype();
        prototype1.setMagnetUri(magnetUri);
        EpisodePrototype prototype2 = new EpisodePrototype();
        prototype2.setMagnetUri("different magnet uri");
        Assert.assertNotEquals(prototype1, prototype2);
        Assert.assertNotEquals(prototype1.hashCode(), prototype2.hashCode());
    }
}
