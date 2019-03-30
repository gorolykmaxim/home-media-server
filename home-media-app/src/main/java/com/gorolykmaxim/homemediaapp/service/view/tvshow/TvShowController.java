package com.gorolykmaxim.homemediaapp.service.view.tvshow;

import com.gorolykmaxim.homemediaapp.common.PathResolver;
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
import com.gorolykmaxim.homemediaapp.service.view.tvshow.episode.ViewableEpisodeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("tv-show")
public class TvShowController {
    private PathResolver torrentPathResolver;
    private TvShowRepository tvShowRepository;
    private ThumbnailRepository thumbnailRepository;
    private EpisodeRepository episodeRepository;
    private TvShowFactory tvShowFactory;
    private TorrentFactory torrentFactory;
    private TorrentRepository torrentRepository;
    private EpisodeViewRepository episodeViewRepository;
    private ViewableEpisodeFactory viewableEpisodeFactory;

    @Autowired
    public TvShowController(TvShowRepository tvShowRepository, ThumbnailRepository thumbnailRepository,
                            EpisodeRepository episodeRepository, TvShowFactory tvShowFactory,
                            TorrentFactory torrentFactory, TorrentRepository torrentRepository,
                            EpisodeViewRepository episodeViewRepository) {
        this.tvShowRepository = tvShowRepository;
        this.thumbnailRepository = thumbnailRepository;
        this.episodeRepository = episodeRepository;
        this.tvShowFactory = tvShowFactory;
        this.torrentFactory = torrentFactory;
        this.torrentRepository = torrentRepository;
        this.episodeViewRepository = episodeViewRepository;
        viewableEpisodeFactory = new ViewableEpisodeFactory();
    }

    @Value("${home-media-app.torrent.default-download-folder:/media/}")
    public void setTorrentDownloadFolder(String torrentDownloadFolder) {
        torrentPathResolver = new PathResolver(torrentDownloadFolder);
    }

    @GetMapping
    public ModelAndView showAll() {
        ModelAndView modelAndView = new ModelAndView("tv-show/list");
        modelAndView.addObject("tvShowList", tvShowRepository.findAll());
        modelAndView.addObject("addShowUrl", "/tv-show/add");
        return modelAndView;
    }

    @GetMapping("{id}")
    public ModelAndView show(@PathVariable UUID id) {
        try {
            TvShow tvShow = tvShowRepository.findById(id);
            List<Episode> episodeList = episodeRepository.findByTvShow(tvShow);
            List<String> episodeNames = episodeList
                    .stream()
                    .map(Episode::getName)
                    .collect(Collectors.toList());
            Set<String> episodeViewList = episodeViewRepository.findAllByEpisodeNameIn(episodeNames)
                    .stream()
                    .map(EpisodeView::getEpisodeName)
                    .collect(Collectors.toSet());
            List<ViewableEpisode> viewableEpisodeList = new ArrayList<>();
            for (Episode episode: episodeList) {
                ViewableEpisode viewableEpisode = viewableEpisodeFactory.create(episode.getName(), episodeViewList.contains(episode.getName()));
                viewableEpisodeList.add(viewableEpisode);
            }
            ModelAndView modelAndView = new ModelAndView("tv-show/view");
            modelAndView.addObject("tvShow", tvShow);
            modelAndView.addObject("episodeList", viewableEpisodeList);
            modelAndView.addObject("editTvShowUrl", String.format("/tv-show/%s/edit", id));
            modelAndView.addObject("deleteTvShowUrl", String.format("/tv-show/%s/delete", id));
            modelAndView.addObject("episodeAddUrl", String.format("/tv-show/%s/episode/add", id));
            return modelAndView;
        } catch (RuntimeException | TvShowRepository.TvShowDoesNotExistException e) {
            throw new ViewError(e);
        }
    }

    @GetMapping("{id}/edit")
    public ModelAndView showEditTvShow(@PathVariable UUID id) {
        try {
            TvShow tvShow = tvShowRepository.findById(id);
            ModelAndView modelAndView = new ModelAndView("tv-show/edit");
            modelAndView.addObject("actionType", "Edit");
            modelAndView.addObject("submitUrl", String.format("/tv-show/%s/edit", id));
            modelAndView.addObject("cancelUrl", String.format("/tv-show/%s", id));
            modelAndView.addObject("name", tvShow.getName());
            return modelAndView;
        } catch (RuntimeException | TvShowRepository.TvShowDoesNotExistException e) {
            throw new ViewError(e);
        }
    }

    @PostMapping("{id}/edit")
    public String editTvShow(@PathVariable UUID id, TvShowPrototype prototype) {
        try {
            TvShow tvShow = tvShowRepository.findById(id);
            tvShow.setName(prototype.getName());
            tvShowRepository.save(tvShow);
            return String.format("redirect:/tv-show/%s/thumbnail/edit/1", id);
        } catch (RuntimeException | TvShowRepository.TvShowDoesNotExistException e) {
            throw new ViewError(e);
        }
    }

    @GetMapping("{id}/delete")
    public String deleteTvShow(@PathVariable UUID id) {
        try {
            TvShow tvShow = tvShowRepository.findById(id);
            tvShowRepository.delete(tvShow);
            return "redirect:/tv-show";
        } catch (RuntimeException | TvShowRepository.TvShowDoesNotExistException e) {
            throw new ViewError(e);
        }
    }

    @GetMapping("{id}/episode/{name}/delete")
    public String deleteEpisodeOfTvShow(@PathVariable UUID id, @PathVariable String name) {
        try {
            TvShow tvShow = tvShowRepository.findById(id);
            episodeRepository.deleteEpisodeOfTvShow(tvShow, name);
            return String.format("redirect:/tv-show/%s", id);
        } catch (RuntimeException | TvShowRepository.TvShowDoesNotExistException e) {
            throw new ViewError(e);
        }
    }

    @GetMapping("add")
    public ModelAndView showAddForm() {
        ModelAndView modelAndView = new ModelAndView("tv-show/edit");
        modelAndView.addObject("actionType", "Add");
        modelAndView.addObject("submitUrl", "/tv-show/add");
        modelAndView.addObject("cancelUrl", "/");
        modelAndView.addObject("name", null);
        return modelAndView;
    }

    @PostMapping("add")
    public String addShow(TvShowPrototype prototype) {
        try {
            TvShow tvShow = tvShowFactory.create(prototype.getName());
            tvShowRepository.save(tvShow);
            return String.format("redirect:/tv-show/%s/thumbnail/edit/1", tvShow.getId());
        } catch (RuntimeException e) {
            throw new ViewError(e);
        }
    }

    @GetMapping("{id}/episode/add")
    public ModelAndView showAddEpisodeForm(@PathVariable UUID id) {
        try {
            TvShow tvShow = tvShowRepository.findById(id);
            ModelAndView modelAndView = new ModelAndView("episode/add");
            modelAndView.addObject("addEpisodeUrl", String.format("/tv-show/%s/episode/add", id));
            modelAndView.addObject("cancelUrl", String.format("/tv-show/%s", id));
            modelAndView.addObject("tvShow", tvShow);
            return modelAndView;
        } catch (RuntimeException | TvShowRepository.TvShowDoesNotExistException e) {
            throw new ViewError(e);
        }
    }

    @PostMapping("{id}/episode/add")
    public String addEpisode(@PathVariable UUID id, EpisodePrototype prototype) {
        try {
            TvShow tvShow = tvShowRepository.findById(id);
            Torrent torrent = torrentFactory.createMagnet(prototype.getMagnetUri(), torrentPathResolver.resolve(tvShow.getDirectory()));
            torrentRepository.save(torrent);
            return "redirect:/downloads";
        } catch (RuntimeException | TvShowRepository.TvShowDoesNotExistException e) {
            throw new ViewError(e);
        }
    }

    @GetMapping("{id}/thumbnail/edit/{index}")
    public ModelAndView showThumbnailEditForm(@PathVariable UUID id, @PathVariable int index) {
        try {
            TvShow tvShow = tvShowRepository.findById(id);
            Optional<Thumbnail> possibleThumbnail = thumbnailRepository.findThumbnailBySearchTermAndIndex(tvShow.getName(), index - 1);
            ModelAndView modelAndView = new ModelAndView("tv-show/thumbnail/edit");
            int nextIndex;
            if (possibleThumbnail.isPresent()) {
                // Pick the next thumbnail
                nextIndex = index + 1;
                modelAndView.addObject("thumbnailUrl", possibleThumbnail.get().getUri());
            } else if (index > 1) {
                // We have shown all available thumbnails. Let's show them all over again.
                return new ModelAndView(String.format("redirect:/tv-show/%s/thumbnail/edit/%s", id, 1));
            } else {
                // No thumbnail has been found. Just notify user about it.
                nextIndex = 1;
                modelAndView.addObject("thumbnailUrl", tvShow.getThumbnail());
                modelAndView.addObject("thumbnailNotFoundMessage", "I was not able to find a " +
                        "thumbnail for the show, so i've assigned a default one. " +
                        "You may want to change TV Show name, so i'll try again.");
            }
            modelAndView.addObject("submitUrl", String.format("/tv-show/%s/thumbnail/save", id));
            modelAndView.addObject("nextThumbnailUrl", String.format("/tv-show/%s/thumbnail/edit/%s", id, nextIndex));
            modelAndView.addObject("cancelUrl", String.format("/tv-show/%s", id));
            return modelAndView;
        } catch (RuntimeException | TvShowRepository.TvShowDoesNotExistException e) {
            throw new ViewError(e);
        }
    }

    @PostMapping("{id}/thumbnail/save")
    public String saveThumbnail(@PathVariable UUID id, ThumbnailPrototype prototype) {
        try {
            TvShow tvShow = tvShowRepository.findById(id);
            tvShow.setThumbnail(prototype.getThumbnail());
            tvShowRepository.save(tvShow);
            return String.format("redirect:/tv-show/%s", id);
        } catch (RuntimeException | TvShowRepository.TvShowDoesNotExistException e) {
            throw new ViewError(e);
        }
    }
}