package com.gorolykmaxim.homemediaapp.model.tvshow;

import java.util.Optional;

public interface ThumbnailRepository {
    Optional<Thumbnail> findThumbnailBySearchTermAndIndex(String searchTerm, int index);
}
