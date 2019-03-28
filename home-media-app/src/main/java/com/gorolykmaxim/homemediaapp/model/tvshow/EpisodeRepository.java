package com.gorolykmaxim.homemediaapp.model.tvshow;

import java.util.List;

public interface EpisodeRepository {
    List<Episode> findByTvShow(TvShow tvShow);
    void deleteEpisodeOfShow(TvShow tvShow, String episodeName);
}
