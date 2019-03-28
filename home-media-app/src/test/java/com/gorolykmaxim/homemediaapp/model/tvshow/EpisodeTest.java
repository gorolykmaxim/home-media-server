package com.gorolykmaxim.homemediaapp.model.tvshow;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EpisodeTest {
    private EpisodeFactory factory;
    private String name;

    @Before
    public void setUp() throws Exception {
        factory = new EpisodeFactory();
        name = "Episode 5";
    }

    @Test
    public void create() {
        Episode episode = factory.create(name);
        Assert.assertEquals(name, episode.getName());
    }

    @Test
    public void twoEpisodesAreSame() {
        Episode episode1 = factory.create(name);
        Episode episode2 = factory.create(name);
        Assert.assertEquals(episode1, episode2);
        Assert.assertEquals(episode1.hashCode(), episode2.hashCode());
    }

    @Test
    public void twoEpisodesAreDifferent() {
        Episode episode1 = factory.create(name);
        Episode episode2 = factory.create(name + "2");
        Assert.assertNotEquals(episode1, episode2);
        Assert.assertNotEquals(episode1.hashCode(), episode2.hashCode());
    }
}
