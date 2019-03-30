package com.gorolykmaxim.homemediaapp.model.tvshow.episode;

import com.gorolykmaxim.homemediaapp.model.tvshow.TvShow;

import java.util.List;
import java.util.stream.Collectors;

public class EpisodeRepository {
    private EpisodeStorage storage;
    private EpisodeFactory factory;

    public EpisodeRepository(EpisodeStorage storage, EpisodeFactory factory) {
        this.storage = storage;
        this.factory = factory;
    }

    public List<Episode> findByTvShow(TvShow tvShow) {
        return storage.findEpisodeNamesByTvShow(tvShow).stream().map(factory::create).collect(Collectors.toList());
    }

    public void deleteEpisodeOfTvShow(TvShow tvShow, String episodeName) {
        storage.deleteEpisodeOfShow(tvShow, episodeName);
    }
}
