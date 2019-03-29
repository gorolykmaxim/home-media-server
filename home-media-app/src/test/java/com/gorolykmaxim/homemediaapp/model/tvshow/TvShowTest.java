package com.gorolykmaxim.homemediaapp.model.tvshow;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TvShowTest {
    private TvShowFactory factory;
    private URI defaultThumbnail;
    private String name;
    private TvShowDao dao;
    private TvShowStorage storage;
    private TvShowRepository repository;

    @Before
    public void setUp() throws Exception {
        defaultThumbnail = URI.create("https://instagram.com/photo.png");
        name = "show name";
        factory = new TvShowFactory(defaultThumbnail);
        dao = Mockito.mock(TvShowDao.class);
        storage = Mockito.mock(TvShowStorage.class);
        repository = new TvShowRepository(storage, dao);
    }

    @Test
    public void createWithDefaultThumbnail() {
        TvShow tvShow = factory.create(name);
        Assert.assertNotNull(tvShow.getId());
        Assert.assertEquals(name, tvShow.getName());
        Assert.assertEquals(name, tvShow.getDirectory());
        Assert.assertEquals(defaultThumbnail, tvShow.getThumbnail());
    }

    @Test
    public void createWithCustomThumbnail() {
        URI uri = URI.create("http://facebook.com/profile.png");
        Thumbnail thumbnail = Mockito.mock(Thumbnail.class);
        Mockito.when(thumbnail.getUri()).thenReturn(uri);
        TvShow tvShow = factory.create(name, thumbnail);
        Assert.assertNotNull(tvShow.getId());
        Assert.assertEquals(name, tvShow.getName());
        Assert.assertEquals(name, tvShow.getDirectory());
        Assert.assertEquals(uri, tvShow.getThumbnail());
    }

    @Test
    public void setName() {
        TvShow tvShow = factory.create(name);
        String differentName = "different name";
        tvShow.setName(differentName);
        Assert.assertEquals(differentName, tvShow.getName());
    }

    @Test
    public void setThumbnail() {
        TvShow tvShow = factory.create(name);
        URI uri = URI.create("http://facebook.com/profile.png");
        tvShow.setThumbnail(uri);
        Assert.assertEquals(uri, tvShow.getThumbnail());
    }

    @Test
    public void tvShowsAreDifferent() {
        TvShow tvShow1 = factory.create(name);
        TvShow tvShow2 = factory.create(name);
        Assert.assertNotEquals(tvShow1, tvShow2);
        Assert.assertNotEquals(tvShow1.hashCode(), tvShow2.hashCode());
    }

    @Test
    public void findAll() {
        Mockito.when(dao.findAll()).thenReturn(Collections.emptyList());
        Iterable<TvShow> tvShowList = repository.findAll();
        Assert.assertEquals(Collections.emptyList(), tvShowList);
    }

    @Test
    public void findById() throws TvShowRepository.TvShowDoesNotExistException {
        UUID id = UUID.randomUUID();
        TvShow expectedTvShow = factory.create(name);
        Mockito.when(dao.findById(id)).thenReturn(Optional.of(expectedTvShow));
        TvShow tvShow = repository.findById(id);
        Assert.assertEquals(expectedTvShow, tvShow);
    }

    @Test(expected = TvShowRepository.TvShowDoesNotExistException.class)
    public void failToFindById() throws TvShowRepository.TvShowDoesNotExistException {
        UUID id = UUID.randomUUID();
        Mockito.when(dao.findById(id)).thenReturn(Optional.empty());
        repository.findById(id);
    }

    @Test
    public void save() {
        TvShow tvShow = factory.create(name);
        repository.save(tvShow);
        Mockito.verify(dao).save(tvShow);
        Mockito.verify(storage).store(tvShow);
    }

    @Test
    public void delete() {
        TvShow tvShow = factory.create(name);
        repository.delete(tvShow);
        Mockito.verify(dao).delete(tvShow);
        Mockito.verify(storage).delete(tvShow);
    }
}
