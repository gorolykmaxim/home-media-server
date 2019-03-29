package com.gorolykmaxim.homemediaapp.model.tvshow.episode;

import java.util.Objects;

public class Episode {
    private String name;

    Episode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Episode episode = (Episode) o;
        return Objects.equals(name, episode.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
