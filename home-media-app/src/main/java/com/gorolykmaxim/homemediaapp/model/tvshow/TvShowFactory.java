package com.gorolykmaxim.homemediaapp.model.tvshow;

import java.net.URI;
import java.util.UUID;

public class TvShowFactory {
    private URI defaultThumbnail;

    public TvShowFactory(URI defaultThumbnail) {
        this.defaultThumbnail = defaultThumbnail;
    }

    public TvShow create(String name, Thumbnail thumbnail) {
        return new TvShow(UUID.randomUUID(), name, name, thumbnail.getUri());
    }

    public TvShow create(String name) {
        return new TvShow(UUID.randomUUID(), name, name, defaultThumbnail);
    }
}
