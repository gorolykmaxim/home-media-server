package com.gorolykmaxim.homemediaapp.model.tvshow;

import com.gorolykmaxim.homemediaapp.common.PathResolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URI;

public class TvShowTest {
    private PathResolver pathResolver;
    private TvShowFactory factory;
    private URI defaultThumbnail;
    private String name, directory;

    @Before
    public void setUp() throws Exception {
        defaultThumbnail = URI.create("https://instagram.com/photo.png");
        pathResolver = Mockito.mock(PathResolver.class);
        name = "show name";
        directory = "show directory";
        Mockito.when(pathResolver.resolve(name)).thenReturn(directory);
        factory = new TvShowFactory(pathResolver, defaultThumbnail);
    }

    @Test
    public void createWithDefaultThumbnail() {
        TvShow tvShow = factory.create(name);
        Assert.assertEquals(name, tvShow.getName());
        Assert.assertEquals(directory, tvShow.getDirectory());
        Assert.assertEquals(defaultThumbnail, tvShow.getThumbnail());
    }

    @Test
    public void createWithCustomThumbnail() {
        URI uri = URI.create("http://facebook.com/profile.png");
        Thumbnail thumbnail = Mockito.mock(Thumbnail.class);
        Mockito.when(thumbnail.getUri()).thenReturn(uri);
        TvShow tvShow = factory.create(name, thumbnail);
        Assert.assertEquals(name, tvShow.getName());
        Assert.assertEquals(directory, tvShow.getDirectory());
        Assert.assertEquals(uri, tvShow.getThumbnail());
    }

    @Test
    public void tvShowsAreDifferent() {
        TvShow tvShow1 = factory.create(name);
        TvShow tvShow2 = factory.create(name);
        Assert.assertNotEquals(tvShow1, tvShow2);
        Assert.assertNotEquals(tvShow1.hashCode(), tvShow2.hashCode());
    }
}
