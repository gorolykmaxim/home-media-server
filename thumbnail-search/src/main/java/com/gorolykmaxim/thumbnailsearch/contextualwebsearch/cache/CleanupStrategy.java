package com.gorolykmaxim.thumbnailsearch.contextualwebsearch.cache;

public interface CleanupStrategy {
    void clean(CachedImageRepository repository);
}