package com.gorolykmaxim.homemediaapp.service.view.tvshow.episode;

import java.util.Objects;

public class EpisodePrototype {
    private String magnetUri;

    public String getMagnetUri() {
        return magnetUri;
    }

    public void setMagnetUri(String magnetUri) {
        this.magnetUri = magnetUri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EpisodePrototype that = (EpisodePrototype) o;
        return Objects.equals(magnetUri, that.magnetUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(magnetUri);
    }
}
