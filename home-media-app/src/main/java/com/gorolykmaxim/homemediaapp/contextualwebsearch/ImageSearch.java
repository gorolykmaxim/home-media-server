package com.gorolykmaxim.homemediaapp.contextualwebsearch;

import com.gorolykmaxim.homemediaapp.contextualwebsearch.api.ImageList;
import com.gorolykmaxim.homemediaapp.contextualwebsearch.api.ImageSearchApi;
import com.gorolykmaxim.homemediaapp.contextualwebsearch.cache.CachedImage;
import com.gorolykmaxim.homemediaapp.contextualwebsearch.cache.ImageCache;
import com.gorolykmaxim.homemediaapp.model.tvshow.Thumbnail;
import com.gorolykmaxim.homemediaapp.model.tvshow.ThumbnailRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImageSearch implements ThumbnailRepository {

    private ImageCache cache;
    private ImageSearchApi searchApi;

    public ImageSearch(ImageCache cache, ImageSearchApi searchApi) {
        this.cache = cache;
        this.searchApi = searchApi;
    }

    @Override
    public Optional<Thumbnail> findThumbnailBySearchTermAndIndex(String searchTerm, int index) {
        Optional<CachedImage> possibleCachedImage = cache.findBySearchTermAndIndex(searchTerm, index);
        if (!possibleCachedImage.isPresent()) {
            if (index > 0) {
                return Optional.empty();
            } else {
                ImageList imageList = searchApi.findImagesBySearchTerm(searchTerm);
                if (imageList.getImages().size() == 0) {
                    return Optional.empty();
                } else {
                    cache.save(imageList, searchTerm);
                    return Optional.of(imageList.getImages().get(0));
                }
            }
        } else {
            return Optional.of(possibleCachedImage.get());
        }
    }

    @Override
    public List<Thumbnail> findThumbnailsBySearchTerm(String searchTerm) {
        return new ArrayList<>(searchApi.findImagesBySearchTerm(searchTerm).getImages());
    }
}
