package com.gorolykmaxim.homemediaapp.model.tvshow;

import com.gorolykmaxim.homemediaapp.model.tvshow.episode.Episode;
import com.gorolykmaxim.homemediaapp.model.tvshow.episode.EpisodeFactory;
import com.gorolykmaxim.homemediaapp.model.tvshow.episode.EpisodeRepository;
import com.gorolykmaxim.homemediaapp.model.tvshow.episode.EpisodeStorage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

public class EpisodeTest {
    private EpisodeStorage storage;
    private EpisodeRepository repository;
    private EpisodeFactory factory;
    private String name;

    @Before
    public void setUp() throws Exception {
        factory = new EpisodeFactory();
        storage = Mockito.mock(EpisodeStorage.class);
        repository = new EpisodeRepository(storage, factory);
        name = "Episode 5";
    }

    @Test
    public void findByTvShow() {
        TvShow tvShow = Mockito.mock(TvShow.class);
        Mockito.when(storage.findEpisodeNamesByTvShow(tvShow)).thenReturn(Collections.singletonList(name));
        List<Episode> episodeList = repository.findByTvShow(tvShow);
        Assert.assertEquals(1, episodeList.size());
        Episode episode = episodeList.get(0);
        Assert.assertEquals(name, episode.getName());
    }

    @Test
    public void deleteEpisodeOfTvShow() {
        TvShow tvShow = Mockito.mock(TvShow.class);
        repository.deleteEpisodeOfTvShow(tvShow, name);
        Mockito.verify(storage).deleteEpisodeOfShow(tvShow, name);
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
