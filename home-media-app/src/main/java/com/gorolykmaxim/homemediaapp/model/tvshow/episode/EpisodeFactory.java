package com.gorolykmaxim.homemediaapp.model.tvshow.episode;

public class EpisodeFactory {
    public Episode create(String name) {
        return new Episode(name);
    }
}
