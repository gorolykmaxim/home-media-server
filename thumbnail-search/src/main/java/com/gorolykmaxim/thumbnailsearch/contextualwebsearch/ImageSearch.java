package com.gorolykmaxim.thumbnailsearch.contextualwebsearch;

import com.gorolykmaxim.thumbnailsearch.contextualwebsearch.api.ImageList;
import com.gorolykmaxim.thumbnailsearch.contextualwebsearch.api.ImageSearchApi;
import com.gorolykmaxim.thumbnailsearch.contextualwebsearch.cache.CachedImage;
import com.gorolykmaxim.thumbnailsearch.contextualwebsearch.cache.ImageCache;
import com.gorolykmaxim.thumbnailsearch.model.Thumbnail;
import com.gorolykmaxim.thumbnailsearch.model.ThumbnailRepository;

import java.util.Optional;

public class ImageSearch implements ThumbnailRepository {

    private ImageCache cache;
    private ImageSearchApi searchApi;

    public ImageSearch(ImageCache cache, ImageSearchApi searchApi) {
        this.cache = cache;
        this.searchApi = searchApi;
    }

    @Override
    public Optional<Thumbnail> findThumbnailBySearchTermAndIndex(String searchTerm) {
        Optional<CachedImage> possibleCachedImage = cache.findBySearchTermAndIndex(searchTerm);
        if (!possibleCachedImage.isPresent()) {
            ImageList imageList = searchApi.findImagesBySearchTerm(searchTerm);
            if (imageList.getImages().size() == 0) {
                return Optional.empty();
            } else {
                cache.save(imageList, searchTerm);
                return Optional.of(imageList.getRandomImage());
            }
        } else {
            return Optional.of(possibleCachedImage.get());
        }
    }
}