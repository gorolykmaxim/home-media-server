package com.gorolykmaxim.homemediaapp.model.tvshow;

import java.net.URI;
import java.util.UUID;

public class TvShowFactory {
    private URI defaultThumbnail;

    public TvShowFactory(URI defaultThumbnail) {
        this.defaultThumbnail = defaultThumbnail;
    }

    public TvShow create(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("TV Show name should not be empty");
        }
        return new TvShow(UUID.randomUUID(), name, name, defaultThumbnail);
    }
}
