package com.gorolykmaxim.homemediaapp.model.tvshow;

import com.gorolykmaxim.homemediaapp.common.PersistableUri;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;

@Entity
public class TvShow {
    @Id
    private UUID id;
    private String name;
    private String directory;
    @Embedded
    private PersistableUri thumbnail;

    protected TvShow() {}

    TvShow(UUID id, String name, String directory, URI thumbnail) {
        this.id = id;
        this.name = name;
        this.directory = directory;
        this.thumbnail = new PersistableUri(thumbnail);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDirectory() {
        return directory;
    }

    public void setThumbnail(URI thumbnail) {
        this.thumbnail = new PersistableUri(thumbnail);
    }

    public URI getThumbnail() {
        return thumbnail.getUri();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TvShow tvShow = (TvShow) o;
        return Objects.equals(id, tvShow.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
