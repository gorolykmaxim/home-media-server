package com.gorolykmaxim.thumbnailsearch.model;

import java.util.Optional;

public interface ThumbnailRepository {
    Optional<Thumbnail> findThumbnailBySearchTermAndIndex(String searchTerm);
}
