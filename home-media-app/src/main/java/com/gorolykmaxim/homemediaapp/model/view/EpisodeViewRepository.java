package com.gorolykmaxim.homemediaapp.model.view;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EpisodeViewRepository extends CrudRepository<EpisodeView, String> {
    List<EpisodeView> findAllByEpisodeNameIn(List<String> episodeNames);
}
