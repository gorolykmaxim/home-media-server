package com.gorolykmaxim.homemediaapp.service.view.tvshow;

import java.net.URI;
import java.util.Objects;

public class ThumbnailPrototype {
    private URI thumbnail;

    public URI getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(URI thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThumbnailPrototype that = (ThumbnailPrototype) o;
        return Objects.equals(thumbnail, that.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(thumbnail);
    }
}
