package com.gorolykmaxim.homemediaapp.model.tvshow.episode;

import com.gorolykmaxim.homemediaapp.model.tvshow.TvShow;

import java.util.List;

public interface EpisodeStorage {
    List<String> findEpisodeNamesByTvShow(TvShow tvShow);
    void deleteEpisodeOfShow(TvShow tvShow, String episodeName);
}
