package com.gorolykmaxim.thumbnailsearch.contextualwebsearch.cache;

import com.gorolykmaxim.thumbnailsearch.common.PersistableUri;
import com.gorolykmaxim.thumbnailsearch.model.Thumbnail;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.sql.Date;
import java.util.Objects;

@Entity
public class CachedImage implements Thumbnail {
    @EmbeddedId
    private PersistableUri uri;
    private String searchTerm;
    private Date creationDate;

    protected CachedImage() {}

    CachedImage(String uri, String searchTerm, Date creationDate) {
        this.uri = new PersistableUri(uri);
        this.searchTerm = searchTerm;
        this.creationDate = creationDate;
    }

    @Override
    public String getUri() {
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