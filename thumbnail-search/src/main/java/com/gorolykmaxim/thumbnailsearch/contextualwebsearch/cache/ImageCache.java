package com.gorolykmaxim.thumbnailsearch.contextualwebsearch.cache;

import com.gorolykmaxim.thumbnailsearch.contextualwebsearch.api.Image;
import com.gorolykmaxim.thumbnailsearch.contextualwebsearch.api.ImageList;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ImageCache {

    private CachedImageRepository repository;
    private CachedImageFactory factory;
    private CleanupStrategy cleanupStrategy;
    private Random random;

    public ImageCache(CachedImageRepository repository, CachedImageFactory factory, CleanupStrategy cleanupStrategy) {
        this.repository = repository;
        this.factory = factory;
        this.cleanupStrategy = cleanupStrategy;
        random = new Random();
    }

    public Optional<CachedImage> findBySearchTerm(String searchTerm) {
        List<CachedImage> cachedImageList = repository.findAllBySearchTermLike(searchTerm);
        if (cachedImageList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(cachedImageList.get(random.nextInt(cachedImageList.size())));
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