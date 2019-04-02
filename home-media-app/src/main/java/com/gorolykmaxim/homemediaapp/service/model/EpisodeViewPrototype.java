package com.gorolykmaxim.homemediaapp.service.model;

import java.util.Objects;

public class EpisodeViewPrototype {
    private String episodeName;

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EpisodeViewPrototype that = (EpisodeViewPrototype) o;
        return Objects.equals(episodeName, that.episodeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(episodeName);
    }
}
