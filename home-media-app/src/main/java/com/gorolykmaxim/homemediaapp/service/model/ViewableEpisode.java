package com.gorolykmaxim.homemediaapp.service.model;

import java.util.Objects;

public class ViewableEpisode {
    private String name;
    private boolean viewed;

    public ViewableEpisode(String name, boolean viewed) {
        this.name = name;
        this.viewed = viewed;
    }

    public String getName() {
        return name;
    }

    public boolean isViewed() {
        return viewed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewableEpisode that = (ViewableEpisode) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
