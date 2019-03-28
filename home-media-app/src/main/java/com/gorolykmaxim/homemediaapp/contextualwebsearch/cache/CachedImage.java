package com.gorolykmaxim.homemediaapp.contextualwebsearch.cache;

import com.gorolykmaxim.homemediaapp.common.PersistableUri;
import com.gorolykmaxim.homemediaapp.model.tvshow.Thumbnail;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.net.URI;
import java.sql.Date;
import java.util.Objects;

@Entity
public class CachedImage implements Thumbnail {
    @EmbeddedId
    private PersistableUri uri;
    private String searchTerm;
    private Date creationDate;

    protected CachedImage() {}

    CachedImage(URI uri, String searchTerm, Date creationDate) {
        this.uri = new PersistableUri(uri);
        this.searchTerm = searchTerm;
        this.creationDate = creationDate;
    }

    @Override
    public URI getUri() {
        return uri.getUri();
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CachedImage image = (CachedImage) o;
        return Objects.equals(uri, image.uri) &&
                Objects.equals(searchTerm, image.searchTerm) &&
                Objects.equals(creationDate, image.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, searchTerm, creationDate);
    }
}
