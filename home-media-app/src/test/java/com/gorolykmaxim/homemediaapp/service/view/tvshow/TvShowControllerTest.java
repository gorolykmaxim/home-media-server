package com.gorolykmaxim.homemediaapp.service.view.tvshow;

import com.gorolykmaxim.homemediaapp.model.torrent.Torrent;
import com.gorolykmaxim.homemediaapp.model.torrent.TorrentFactory;
import com.gorolykmaxim.homemediaapp.model.torrent.TorrentRepository;
import com.gorolykmaxim.homemediaapp.model.tvshow.*;
import com.gorolykmaxim.homemediaapp.model.tvshow.episode.Episode;
import com.gorolykmaxim.homemediaapp.model.tvshow.episode.EpisodeRepository;
import com.gorolykmaxim.homemediaapp.model.view.EpisodeView;
import com.gorolykmaxim.homemediaapp.model.view.EpisodeViewRepository;
import com.gorolykmaxim.homemediaapp.service.view.ViewError;
import com.gorolykmaxim.homemediaapp.service.view.tvshow.episode.EpisodePrototype;
import com.gorolykmaxim.homemediaapp.service.view.tvshow.episode.ViewableEpisode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.util.*;

public class TvShowControllerTest {
    private TvShowRepository tvShowRepository;
    private ThumbnailRepository thumbnailRepository;
    private EpisodeRepository episodeRepository;
    private TvShowFactory tvShowFactory;
    private TorrentFactory torrentFactory;
    private TorrentRepository torrentRepository;
    private EpisodeViewRepository episodeViewRepository;
    private TvShowController controller;
    private String torrentRootDirectory;
    private UUID id;
    private TvShow tvShow;
    private Episode episode;
    private EpisodeView episodeView;
    private Torrent torrent;
    private Thumbnail thumbnail;
    private TvShowPrototype tvShowPrototype;
    private EpisodePrototype episodePrototype;
    private ThumbnailPrototype thumbnailPrototype;

    @Before
    public void setUp() throws Exception {
        id = UUID.randomUUID();
        torrentRootDirectory = "/downloads/";
        tvShow = Mockito.mock(TvShow.class);
        Mockito.when(tvShow.getId()).thenReturn(id);
        Mockito.when(tvShow.getName()).thenReturn("Popular TV");
        Mockito.when(tvShow.getDirectory()).thenReturn("popular-tv");
        Mockito.when(tvShow.getThumbnail()).thenReturn(URI.create("http://stock.com/placeholder.png"));
        episode = Mockito.mock(Episode.class);
        Mockito.when(episode.getName()).thenReturn("episode 5");
        episodeView = Mockito.mock(EpisodeView.class);
        Mockito.when(episodeView.getEpisodeName()).thenReturn("episode 5");
        torrent = Mockito.mock(Torrent.class);
        thumbnail = Mockito.mock(Thumbnail.class);
        Mockito.when(thumbnail.getUri()).thenReturn(URI.create("https://instagram.com/face.png"));
        tvShowPrototype = new TvShowPrototype();
        tvShowPrototype.setName(tvShow.getName());
        episodePrototype = new EpisodePrototype();
        episodePrototype.setMagnetUri("magnet uri:");
        thumbnailPrototype = new ThumbnailPrototype();
        thumbnailPrototype.setThumbnail(thumbnail.getUri());
        tvShowRepository = Mockito.mock(TvShowRepository.class);
        thumbnailRepository = Mockito.mock(ThumbnailRepository.class);
        episodeRepository = Mockito.mock(EpisodeRepository.class);
        tvShowFactory = Mockito.mock(TvShowFactory.class);
        torrentFactory = Mockito.mock(TorrentFactory.class);
        torrentRepository = Mockito.mock(TorrentRepository.class);
        episodeViewRepository = Mockito.mock(EpisodeViewRepository.class);
        controller = new TvShowController(tvShowRepository, thumbnailRepository, episodeRepository, tvShowFactory,
                torrentFactory, torrentRepository, episodeViewRepository);
        controller.setTorrentDownloadFolder(torrentRootDirectory);
    }

    @Test
    public void showAll() {
        List<TvShow> shows = Collections.singletonList(Mockito.mock(TvShow.class));
        Mockito.when(tvShowRepository.findAll()).thenReturn(shows);
        ModelAndView modelAndView = controller.showAll();
        Assert.assertEquals("tv-show/list", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        Assert.assertEquals(shows, model.get("tvShowList"));
        Assert.assertEquals("/tv-show/add", model.get("addShowUrl"));
    }

    @Test
    public void show() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        Episode episode2 = Mockito.mock(Episode.class);
        Mockito.when(episode2.getName()).thenReturn("episode 15");
        Mockito.when(episodeRepository.findByTvShow(tvShow)).thenReturn(Arrays.asList(episode, episode2));
        Mockito.when(episodeViewRepository.findAllByEpisodeNameIn(Arrays.asList("episode 5", "episode 15")))
                .thenReturn(Collections.singletonList(episodeView));
        List<ViewableEpisode> expectedEpisodes = new ArrayList<>();
        expectedEpisodes.add(new ViewableEpisode(episode.getName(), true));
        expectedEpisodes.add(new ViewableEpisode(episode2.getName(), false));
        ModelAndView modelAndView = controller.show(id);
        Assert.assertEquals("tv-show/view", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        Assert.assertEquals(tvShow, model.get("tvShow"));
        Assert.assertEquals(expectedEpisodes, model.get("episodeList"));
        Assert.assertEquals(String.format("/tv-show/%s/edit", id), model.get("editTvShowUrl"));
        Assert.assertEquals(String.format("/tv-show/%s/delete", id), model.get("deleteTvShowUrl"));
        Assert.assertEquals(String.format("/tv-show/%s/episode/add", id), model.get("episodeAddUrl"));
    }

    @Test(expected = ViewError.class)
    public void showTvShowDoesNotExist() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenThrow(Mockito.mock(TvShowRepository.TvShowDoesNotExistException.class));
        controller.show(id);
    }

    @Test(expected = ViewError.class)
    public void showFail() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenThrow(Mockito.mock(RuntimeException.class));
        controller.show(id);
    }

    @Test
    public void showEditTvShow() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        ModelAndView modelAndView = controller.showEditTvShow(id);
        Assert.assertEquals("tv-show/edit", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        Assert.assertEquals("Edit", model.get("actionType"));
        Assert.assertEquals(String.format("/tv-show/%s/edit", id), model.get("submitUrl"));
        Assert.assertEquals(String.format("/tv-show/%s", id), model.get("cancelUrl"));
        Assert.assertEquals(tvShow.getName(), model.get("name"));
    }

    @Test(expected = ViewError.class)
    public void showEditTvShowShowDoesNotExist() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(TvShowRepository.TvShowDoesNotExistException.class));
        controller.showEditTvShow(id);
    }

    @Test(expected = ViewError.class)
    public void showEditTvShowFail() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(RuntimeException.class));
        controller.showEditTvShow(id);
    }

    @Test
    public void editTvShow() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        String url = controller.editTvShow(id, tvShowPrototype);
        Assert.assertEquals(String.format("redirect:/tv-show/%s/thumbnail/edit/1", id), url);
        Mockito.verify(tvShow).setName(tvShowPrototype.getName());
        Mockito.verify(tvShowRepository).save(tvShow);
    }

    @Test(expected = ViewError.class)
    public void editTvShowShowDoesNotExist() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(TvShowRepository.TvShowDoesNotExistException.class));
        controller.editTvShow(id, tvShowPrototype);
    }

    @Test(expected = ViewError.class)
    public void editTvShowFail() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(RuntimeException.class));
        controller.editTvShow(id, tvShowPrototype);
    }

    @Test
    public void deleteTvShow() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        String url = controller.deleteTvShow(id);
        Assert.assertEquals("redirect:/tv-show", url);
        Mockito.verify(tvShowRepository).delete(tvShow);
    }

    @Test(expected = ViewError.class)
    public void deleteTvShowShowDoesNotExist() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(TvShowRepository.TvShowDoesNotExistException.class));
        controller.deleteTvShow(id);
    }

    @Test(expected = ViewError.class)
    public void deleteTvShowFail() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(RuntimeException.class));
        controller.deleteTvShow(id);
    }

    @Test
    public void deleteEpisodeOfTvShow() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        String url = controller.deleteEpisodeOfTvShow(id, episode.getName());
        Assert.assertEquals(String.format("redirect:/tv-show/%s", id), url);
        Mockito.verify(episodeRepository).deleteEpisodeOfShow(tvShow, episode.getName());
    }

    @Test(expected = ViewError.class)
    public void deleteEpisodeOfTvShowShowDoesNotExist() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(TvShowRepository.TvShowDoesNotExistException.class));
        controller.deleteEpisodeOfTvShow(id, episode.getName());
    }

    @Test(expected = ViewError.class)
    public void deleteEpisodeOfTvShowFail() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(RuntimeException.class));
        controller.deleteEpisodeOfTvShow(id, episode.getName());
    }

    @Test
    public void showAddForm() {
        ModelAndView modelAndView = controller.showAddForm();
        Assert.assertEquals("tv-show/edit", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        Assert.assertEquals("Add", model.get("actionType"));
        Assert.assertEquals("/tv-show/add", model.get("submitUrl"));
        Assert.assertEquals("/", model.get("cancelUrl"));
        Assert.assertNull(model.get("name"));
    }

    @Test
    public void addShow() {
        Mockito.when(tvShowFactory.create(tvShowPrototype.getName())).thenReturn(tvShow);
        String url = controller.addShow(tvShowPrototype);
        Assert.assertEquals(String.format("redirect:/tv-show/%s/thumbnail/edit/1", id), url);
        Mockito.verify(tvShowRepository).save(tvShow);
    }

    @Test(expected = ViewError.class)
    public void addShowFail() {
        Mockito.when(tvShowFactory.create(tvShowPrototype.getName())).thenReturn(tvShow);
        Mockito.doThrow(Mockito.mock(RuntimeException.class)).when(tvShowRepository).save(tvShow);
        controller.addShow(tvShowPrototype);
    }

    @Test
    public void showAddEpisodeForm() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        ModelAndView modelAndView = controller.showAddEpisodeForm(id);
        Assert.assertEquals("episode/add", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        Assert.assertEquals(String.format("/tv-show/%s/episode/add", id), model.get("addEpisodeUrl"));
        Assert.assertEquals(String.format("/tv-show/%s", id), model.get("cancelUrl"));
        Assert.assertEquals(tvShow, model.get("tvShow"));
    }

    @Test(expected = ViewError.class)
    public void showAddEpisodeFormShowDoesNotExist() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(TvShowRepository.TvShowDoesNotExistException.class));
        controller.showAddEpisodeForm(id);
    }

    @Test(expected = ViewError.class)
    public void showAddEpisodeFormFail() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(RuntimeException.class));
        controller.showAddEpisodeForm(id);
    }

    @Test
    public void addEpisode() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        Mockito.when(torrentFactory.createMagnet(episodePrototype.getMagnetUri(), "/downloads/popular-tv"))
                .thenReturn(torrent);
        String url = controller.addEpisode(id, episodePrototype);
        Assert.assertEquals("redirect:/downloads", url);
        Mockito.verify(torrentRepository).save(torrent);
    }

    @Test(expected = ViewError.class)
    public void addEpisodeShowDoesNotExist() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(TvShowRepository.TvShowDoesNotExistException.class));
        controller.addEpisode(id, episodePrototype);
    }

    @Test(expected = ViewError.class)
    public void addEpisodeFail() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(RuntimeException.class));
        controller.addEpisode(id, episodePrototype);
    }

    @Test
    public void saveThumbnail() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        String url = controller.saveThumbnail(id, thumbnailPrototype);
        Assert.assertEquals(String.format("redirect:/tv-show/%s", id), url);
        Mockito.verify(tvShow).setThumbnail(thumbnailPrototype.getThumbnail());
        Mockito.verify(tvShowRepository).save(tvShow);
    }

    @Test(expected = ViewError.class)
    public void saveThumbnailShowDoesNotExist() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(TvShowRepository.TvShowDoesNotExistException.class));
        controller.saveThumbnail(id, thumbnailPrototype);
    }

    @Test(expected = ViewError.class)
    public void saveThumbnailFail() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(RuntimeException.class));
        controller.saveThumbnail(id, thumbnailPrototype);
    }

    @Test
    public void showThumbnailEditFromFirstTime() throws TvShowRepository.TvShowDoesNotExistException {
        int index = 1;
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        Mockito.when(thumbnailRepository.findThumbnailBySearchTermAndIndex(tvShow.getName(), index - 1))
                .thenReturn(Optional.of(thumbnail));
        ModelAndView modelAndView = controller.showThumbnailEditForm(id, index);
        Assert.assertEquals("tv-show/thumbnail/edit", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        assertThumbnailEditUrls(model, index + 1);
        Assert.assertEquals(thumbnail.getUri(), model.get("thumbnailUrl"));
    }

    @Test
    public void showThumbnailEditFormOutOfThumbnails() throws TvShowRepository.TvShowDoesNotExistException {
        int index = 5;
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        Mockito.when(thumbnailRepository.findThumbnailBySearchTermAndIndex(tvShow.getName(), index - 1))
                .thenReturn(Optional.empty());
        ModelAndView modelAndView = controller.showThumbnailEditForm(id, index);
        Assert.assertEquals(String.format("redirect:/tv-show/%s/thumbnail/edit/%s", id, 1), modelAndView.getViewName());
    }

    @Test
    public void showThumbnailEditFormNoThumbnailFound() throws TvShowRepository.TvShowDoesNotExistException {
        int index = 1;
        Mockito.when(tvShowRepository.findById(id)).thenReturn(tvShow);
        Mockito.when(thumbnailRepository.findThumbnailBySearchTermAndIndex(tvShow.getName(), index - 1))
                .thenReturn(Optional.empty());
        ModelAndView modelAndView = controller.showThumbnailEditForm(id, index);
        Assert.assertEquals("tv-show/thumbnail/edit", modelAndView.getViewName());
        Map<String, Object> model = modelAndView.getModel();
        assertThumbnailEditUrls(model, 1);
        Assert.assertEquals(tvShow.getThumbnail(), model.get("thumbnailUrl"));
        Assert.assertEquals("I was not able to find a " +
                "thumbnail for the show, so i've assigned a default one. " +
                "You may want to change TV Show name, so i'll try again.", model.get("thumbnailNotFoundMessage"));
    }

    @Test(expected = ViewError.class)
    public void showThumbnailEditFormShowDoesNotExist() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(TvShowRepository.TvShowDoesNotExistException.class));
        controller.showThumbnailEditForm(id, 1);
    }

    @Test(expected = ViewError.class)
    public void showThumbnailEditFormFail() throws TvShowRepository.TvShowDoesNotExistException {
        Mockito.when(tvShowRepository.findById(id))
                .thenThrow(Mockito.mock(RuntimeException.class));
        controller.showThumbnailEditForm(id, 1);
    }

    private void assertThumbnailEditUrls(Map<String, Object> model, int index) {
        Assert.assertEquals(String.format("/tv-show/%s/thumbnail/save", id), model.get("submitUrl"));
        Assert.assertEquals(String.format("/tv-show/%s/thumbnail/edit/%s", id, index), model.get("nextThumbnailUrl"));
        Assert.assertEquals(String.format("/tv-show/%s", id), model.get("cancelUrl"));
    }
}
