package com.gorolykmaxim.homemediaapp.model.tvshow;

import com.gorolykmaxim.homemediaapp.common.PathResolver;

import java.net.URI;

public class TvShowFactory {
    private PathResolver pathResolver;
    private URI defaultThumbnail;

    public TvShowFactory(PathResolver pathResolver, URI defaultThumbnail) {
        this.pathResolver = pathResolver;
        this.defaultThumbnail = defaultThumbnail;
    }

    public TvShow create(String name, Thumbnail thumbnail) {
        return new TvShow(name, pathResolver.resolve(name), thumbnail.getUri());
    }

    public TvShow create(String name) {
        return new TvShow(name, pathResolver.resolve(name), defaultThumbnail);
    }
}
