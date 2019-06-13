package com.gorolykmaxim.thumbnailsearch.contextualwebsearch.api;

import com.gorolykmaxim.thumbnailsearch.model.Thumbnail;

import java.util.Objects;

public class Image implements Thumbnail {
    private String imageUrl;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String getUri() {
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