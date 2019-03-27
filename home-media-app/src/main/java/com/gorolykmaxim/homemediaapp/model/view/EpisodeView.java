package com.gorolykmaxim.homemediaapp.model.view;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class EpisodeView {
    @Id
    private String episodeName;

    protected EpisodeView() {
    }

    EpisodeView(String episodeName) {
        this.episodeName = episodeName;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EpisodeView that = (EpisodeView) o;
        return Objects.equals(episodeName, that.episodeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(episodeName);
    }
}
