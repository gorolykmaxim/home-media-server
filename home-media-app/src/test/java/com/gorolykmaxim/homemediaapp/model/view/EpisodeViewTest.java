package com.gorolykmaxim.homemediaapp.model.view;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EpisodeViewTest {
    private EpisodeViewFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new EpisodeViewFactory();
    }

    @Test
    public void create() {
        String episodeName = "Stranger things S1 E4";
        EpisodeView episodeView = factory.create(episodeName);
        Assert.assertEquals(episodeName, episodeView.getEpisodeName());
        EpisodeView anotherSimilarView = factory.create(episodeName);
        Assert.assertEquals(episodeView, anotherSimilarView);
        Assert.assertEquals(episodeView.hashCode(), anotherSimilarView.hashCode());
        EpisodeView anotherDifferentEpisodeView = factory.create("Big bang theory S5 E3");
        Assert.assertNotEquals(episodeView, anotherDifferentEpisodeView);
        Assert.assertNotEquals(episodeView.hashCode(), anotherDifferentEpisodeView.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void failToCreateWithNullEpisodeName() {
        factory.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void failToCreateWithEmptyEpisodeName() {
        factory.create("");
    }
}
