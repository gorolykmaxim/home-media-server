package com.gorolykmaxim.homemediaapp.service.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ViewableEpisodeTest {
    private ViewableEpisodeFactory factory;
    private String name;

    @Before
    public void setUp() throws Exception {
        factory = new ViewableEpisodeFactory();
        name = "Episode 15";
    }

    @Test
    public void create() {
        ViewableEpisode episode = factory.create(name, true);
        Assert.assertEquals(name, episode.getName());
        Assert.assertTrue(episode.isViewed());
    }

    @Test
    public void twoEpisodesAreSame() {
        ViewableEpisode episode1 = factory.create(name, true);
        ViewableEpisode episode2 = factory.create(name, false);
        Assert.assertEquals(episode1, episode2);
        Assert.assertEquals(episode1.hashCode(), episode2.hashCode());
    }

    @Test
    public void twoEpisodesAreDifferent() {
        ViewableEpisode episode1 = factory.create(name, true);
        ViewableEpisode episode2 = factory.create("Episode 3", true);
        Assert.assertNotEquals(episode1, episode2);
        Assert.assertNotEquals(episode1.hashCode(), episode2.hashCode());
    }
}
