package com.gorolykmaxim.homemediaapp.contextualwebsearch.api;

import com.gorolykmaxim.homemediaapp.model.tvshow.Thumbnail;

import java.net.URI;
import java.util.Objects;

public class Image implements Thumbnail {
    private URI imageUrl;

    // Trick to make Jackson less harsh on URLs. When you specify URI attribute on entity, de-serialized by Jackson,
    // Jackson does JSON serialization strictly on URLs and fails to serialize URLs that doesn't comply with the standard.
    // In order to avoid it (since those invalid URLs are fine by the target browsers) we will tell Jackson that our
    // URLs are simple strings, so Jackson won't do any assertions.
    public void setImageUrl(String imageUrl) {
        this.imageUrl = URI.create(imageUrl);
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
