package com.gorolykmaxim.homemediaapp.model.tvshow;

public class EpisodeFactory {
    public Episode create(String name) {
        return new Episode(name);
    }
}
