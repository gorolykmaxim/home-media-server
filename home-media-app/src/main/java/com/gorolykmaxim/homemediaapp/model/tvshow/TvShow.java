package com.gorolykmaxim.homemediaapp.model.tvshow;

import com.gorolykmaxim.homemediaapp.common.PersistableUri;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.net.URI;
import java.util.Objects;

@Entity
public class TvShow {
    @Id
    private String name;
    private String directory;
    @Embedded
    private PersistableUri thumbnail;

    protected TvShow() {}

    TvShow(String name, String directory, URI thumbnail) {
        this.name = name;
        this.directory = directory;
        this.thumbnail = new PersistableUri(thumbnail);
    }

    public String getName() {
        return name;
    }

    public String getDirectory() {
        return directory;
    }

    public URI getThumbnail() {
        return thumbnail.getUri();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TvShow tvShow = (TvShow) o;
        return Objects.equals(name, tvShow.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
