package com.gorolykmaxim.homemediaapp.service.api;

import com.gorolykmaxim.homemediaapp.common.PathResolver;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController("apiTvShowController")
@RequestMapping("/api/v1/tv-show")
public class TvShowController {
    private PathResolver torrentPathResolver;
    private TvShowRepository tvShowRepository;
    private EpisodeRepository episodeRepository;
    private TvShowFactory tvShowFactory;
    private TorrentFactory torrentFactory;
    private TorrentRepository torrentRepository;

    public TvShowController(TvShowRepository tvShowRepository, EpisodeRepository episodeRepository,
                            TvShowFactory tvShowFactory, TorrentFactory torrentFactory, TorrentRepository torrentRepository) {
        this.tvShowRepository = tvShowRepository;
        this.episodeRepository = episodeRepository;
        this.tvShowFactory = tvShowFactory;
        this.torrentFactory = torrentFactory;
        this.torrentRepository = torrentRepository;
    }

    @Value("${home-media-app.torrent.default-download-folder}")
    public void setTorrentDownloadFolder(String torrentDownloadFolder) {
        torrentPathResolver = new PathResolver(torrentDownloadFolder);
    }

    @GetMapping
    public List<TvShow> getAll() {
        List<TvShow> tvShows = new ArrayList<>();
        for (TvShow tvShow: tvShowRepository.findAll()) {
            tvShows.add(tvShow);
        }
        return tvShows;
    }

    @GetMapping("{id}")
    public TvShow getById(@PathVariable UUID id) {
        try {
            return tvShowRepository.findById(id);
        } catch (TvShowRepository.TvShowDoesNotExistException e) {
            throw new TvShowDoesNotExistError(e);
        }
    }

    @PostMapping
    public TvShow create(@RequestBody TvShowPrototype prototype) {
        try {
            TvShow tvShow = tvShowFactory.create(prototype.getName());
            tvShowRepository.save(tvShow);
            return tvShow;
        } catch (IllegalArgumentException e) {
            throw new TvShowCreationError(prototype, e);
        }
    }

    @PutMapping("{id}")
    public void updateById(@PathVariable UUID id, @RequestBody TvShowPrototype prototype) {
        try {
            TvShow tvShow = tvShowRepository.findById(id);
            tvShow.setName(prototype.getName());
            tvShowRepository.save(tvShow);
        } catch (TvShowRepository.TvShowDoesNotExistException e) {
            throw new TvShowDoesNotExistError(e);
        }
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable UUID id) {
        try {
            TvShow tvShow = tvShowRepository.findById(id);
            tvShowRepository.delete(tvShow);
        } catch (TvShowRepository.TvShowDoesNotExistException e) {
            throw new TvShowDoesNotExistError(e);
        }
    }

    @PutMapping("{id}/thumbnail")
    public void updateThumbnail(@PathVariable UUID id, @RequestBody ThumbnailPrototype prototype) {
        try {
            TvShow tvShow = tvShowRepository.findById(id);
            tvShow.setThumbnail(prototype.getThumbnail());
            tvShowRepository.save(tvShow);
        } catch (TvShowRepository.TvShowDoesNotExistException e) {
            throw new TvShowDoesNotExistError(e);
        }
    }

    @GetMapping("{id}/episode")
    public List<Episode> getAllEpisodes(@PathVariable UUID id) {
        try {
            TvShow tvShow = tvShowRepository.findById(id);
            return episodeRepository.findByTvShow(tvShow);
        } catch (TvShowRepository.TvShowDoesNotExistException e) {
            throw new TvShowDoesNotExistError(e);
        }
    }

    @PostMapping("{id}/episode")
    public void createEpisode(@PathVariable UUID id, @RequestBody EpisodePrototype prototype) {
        try {
            TvShow tvShow = tvShowRepository.findById(id);
            Torrent torrent = torrentFactory.createMagnet(prototype.getMagnetUri(), torrentPathResolver.resolve(tvShow.getDirectory()));
            torrentRepository.save(torrent);
        } catch (TvShowRepository.TvShowDoesNotExistException e) {
            throw new TvShowDoesNotExistError(e);
        }
    }

    @DeleteMapping("{id}/episode")
    public void deleteEpisode(@PathVariable UUID id, @RequestParam String name) {
        try {
            TvShow tvShow = tvShowRepository.findById(id);
            episodeRepository.deleteEpisodeOfTvShow(tvShow, name);
        } catch (TvShowRepository.TvShowDoesNotExistException e) {
            throw new TvShowDoesNotExistError(e);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class TvShowDoesNotExistError extends RuntimeException {
        public TvShowDoesNotExistError(Throwable cause) {
            super(cause.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class TvShowCreationError extends RuntimeException {
        public TvShowCreationError(TvShowPrototype prototype, Throwable cause) {
            super(String.format("Failed to create tv show '%s'. Reason: %s", prototype, cause.getMessage()), cause);
        }
    }
}
