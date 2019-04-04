package com.gorolykmaxim.homemediaapp.service.api;

import com.gorolykmaxim.homemediaapp.model.torrent.Torrent;
import com.gorolykmaxim.homemediaapp.model.torrent.TorrentFactory;
import com.gorolykmaxim.homemediaapp.model.torrent.TorrentRepository;
import com.gorolykmaxim.homemediaapp.model.tvshow.TvShow;
import com.gorolykmaxim.homemediaapp.model.tvshow.TvShowFactory;
import com.gorolykmaxim.homemediaapp.model.tvshow.TvShowRepository;
import com.gorolykmaxim.homemediaapp.model.tvshow.episode.Episode;
import com.gorolykmaxim.homemediaapp.model.tvshow.episode.EpisodeRepository;
import com.gorolykmaxim.homemediaapp.service.model.EpisodePrototype;
import com.gorolykmaxim.homemediaapp.service.model.ThumbnailPrototype;
import com.gorolykmaxim.homemediaapp.service.model.TvShowPrototype;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class TvShowControllerTest {
    private String downloadFolder;
    private TvShowRepository tvShowRepository;
    private EpisodeRepository episodeRepository;
    private TvShowFactory tvShowFactory;
    private TorrentFactory torrentFactory;
    private TorrentRepository torrentRepository;
    private TvShowController controller;
    private TvShow tvShow;
    private Torrent torrent;
    private UUID id;
    private TvShowPrototype tvShowPrototype;
    private ThumbnailPrototype thumbnailPrototype;
    private EpisodePrototype episodePrototype;
    private String episodeName;

    @Before
    public void setUp() throws Exception {
        tvShow = Mockito.mock(TvShow.class);
        Mockito.when(tvShow.getDirectory()).thenReturn("black-mirror");
        torrent = Mockito.mock(Torrent.class);
        id = UUID.randomUUID();
        downloadFolder = "/media";
        tvShowRepository = Mockito.mock(TvShowRepository.class);
        episodeRepository = Mockito.mock(EpisodeRepository.class);
        tvShowFactory = Mockito.mock(TvShowFactory.class);
        torrentFactory = Mockito.mock(TorrentFactory.class);
        torrentRepository = Mockito.mock(TorrentRepository.class);
        controller = new TvShowController(tvShowRepository, episodeRepository, tvShowFactory, torrentFactory,
                torrentRepository);
        controller.setTorrentDownloadFolder(downloadFolder);
        tvShowPrototype = new TvShowPrototype();
        tvShowPrototype.setName("Black Mirror");
        thumbnailPrototype = new ThumbnailPrototype();
        thumbnailPrototype.setThumbnail(URI.create("http://image.com/show.png"));
        episodePrototype = new EpisodePrototype();
        episodePrototype.setMagnetUri("magnet link");
        episodeName = "EP1S2";
    }

    @Test
    public void getAll() {
        Iterable<TvShow> expectedTvShows = Collections.singletonList(tvShow);
        Mockito.when(tvShowRepository.findAll()).thenReturn(expectedTvShows);
        List<TvShow> tvShows = controller.getAll();
        Assert.assertEquals(expectedTvShows, tvShows);
    }

    @Test
    public void getById() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        Assert.assertEquals(tvShow, controller.getById(id));
    }

    @Test(expected = TvShowController.TvShowDoesNotExistError.class)
    public void failToGetById() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(TvShowRepository.TvShowDoesNotExistException.class));
        controller.getById(id);
    }

    @Test
    public void create() {
        Mockito.when(tvShowFactory.create(tvShowPrototype.getName()))
                .thenReturn(tvShow);
        TvShow actualTvShow = controller.create(tvShowPrototype);
        Assert.assertEquals(tvShow, actualTvShow);
        Mockito.verify(tvShowRepository).save(tvShow);
    }

    @Test(expected = TvShowController.TvShowCreationError.class)
    public void failToCreate() {
        Mockito.when(tvShowFactory.create(tvShowPrototype.getName()))
                .thenThrow(new IllegalArgumentException());
        controller.create(tvShowPrototype);
    }

    @Test
    public void updateById() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        controller.updateById(id, tvShowPrototype);
        Mockito.verify(tvShow).setName(tvShowPrototype.getName());
        Mockito.verify(tvShowRepository).save(tvShow);
    }

    @Test(expected = TvShowController.TvShowDoesNotExistError.class)
    public void failToUpdateById() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(TvShowRepository.TvShowDoesNotExistException.class));
        controller.updateById(id, tvShowPrototype);
    }

    @Test
    public void deleteById() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        controller.deleteById(id);
        Mockito.verify(tvShowRepository).delete(tvShow);
    }

    @Test(expected = TvShowController.TvShowDoesNotExistError.class)
    public void failToDeleteById() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(TvShowRepository.TvShowDoesNotExistException.class));
        controller.deleteById(id);
    }

    @Test
    public void updateThumbnail() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        controller.updateThumbnail(id, thumbnailPrototype);
        Mockito.verify(tvShow).setThumbnail(thumbnailPrototype.getThumbnail());
        Mockito.verify(tvShowRepository).save(tvShow);
    }

    @Test(expected = TvShowController.TvShowDoesNotExistError.class)
    public void failToUpdateThumbnail() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(TvShowRepository.TvShowDoesNotExistException.class));
        controller.updateThumbnail(id, thumbnailPrototype);
    }

    @Test
    public void getAllEpisodes() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        List<Episode> expectedEpisodes = Collections.singletonList(Mockito.mock(Episode.class));
        Mockito.when(episodeRepository.findByTvShow(tvShow)).thenReturn(expectedEpisodes);
        Assert.assertEquals(expectedEpisodes, controller.getAllEpisodes(id));
    }

    @Test(expected = TvShowController.TvShowDoesNotExistError.class)
    public void failToGetAllEpisodes() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(TvShowRepository.TvShowDoesNotExistException.class));
        controller.getAllEpisodes(id);
    }

    @Test
    public void createEpisode() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        Mockito.when(torrentFactory.createMagnet(episodePrototype.getMagnetUri(), "/media/black-mirror"))
                .thenReturn(torrent);
        controller.createEpisode(id, episodePrototype);
        Mockito.verify(torrentRepository).save(torrent);
    }

    @Test(expected = TvShowController.TvShowDoesNotExistError.class)
    public void failToCreateEpisode() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(TvShowRepository.TvShowDoesNotExistException.class));
        controller.createEpisode(id, episodePrototype);
    }

    @Test
    public void deleteEpisode() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        controller.deleteEpisode(id, episodeName);
        Mockito.verify(episodeRepository).deleteEpisodeOfTvShow(tvShow, episodeName);
    }

    @Test(expected = TvShowController.TvShowDoesNotExistError.class)
    public void failToDeleteEpisode() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(TvShowRepository.TvShowDoesNotExistException.class));
        controller.deleteEpisode(id, episodeName);
    }
}
