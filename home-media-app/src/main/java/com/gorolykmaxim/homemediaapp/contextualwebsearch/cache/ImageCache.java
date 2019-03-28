package com.gorolykmaxim.homemediaapp.contextualwebsearch.cache;

import com.gorolykmaxim.homemediaapp.contextualwebsearch.api.Image;
import com.gorolykmaxim.homemediaapp.contextualwebsearch.api.ImageList;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public class ImageCache {

    private CachedImageRepository repository;
    private CachedImageFactory factory;
    private CleanupStrategy cleanupStrategy;

    public ImageCache(CachedImageRepository repository, CachedImageFactory factory, CleanupStrategy cleanupStrategy) {
        this.repository = repository;
        this.factory = factory;
        this.cleanupStrategy = cleanupStrategy;
    }

    public Optional<CachedImage> findBySearchTermAndIndex(String searchTerm, int index) {
        List<CachedImage> cachedImageList = repository.findAllBySearchTermLike(searchTerm, PageRequest.of(index, 1));
        if (cachedImageList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(cachedImageList.get(0));
        }
    }

    public void save(ImageList imageList, String searchTerm) {
        for (Image image: imageList.getImages()) {
            if (factory.isUriSupported(image.getUri())) {
                CachedImage cachedImage = factory.create(image.getUri(), searchTerm);
                repository.save(cachedImage);
            }
        }
        cleanupStrategy.clean(repository);
    }

}
