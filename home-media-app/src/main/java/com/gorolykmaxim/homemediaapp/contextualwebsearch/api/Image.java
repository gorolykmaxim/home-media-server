package com.gorolykmaxim.homemediaapp.contextualwebsearch.api;

import com.gorolykmaxim.homemediaapp.model.tvshow.Thumbnail;

import java.net.URI;
import java.util.Objects;

public class Image implements Thumbnail {
    private URI imageUrl;

    public void setImageUrl(URI imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public URI getUri() {
        return imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(imageUrl, image.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrl);
    }
}
