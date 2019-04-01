package com.gorolykmaxim.homemediaapp.contextualwebsearch;

import com.gorolykmaxim.homemediaapp.contextualwebsearch.api.ImageSearchApi;
import com.gorolykmaxim.homemediaapp.contextualwebsearch.cache.*;
import com.gorolykmaxim.homemediaapp.model.tvshow.ThumbnailRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ContextualWebSearchConfiguration {

    @Value("${home-media-app.contextualwebsearch.base-uri}")
    private String baseUri;
    @Value("${home-media-app.contextualwebsearch.page-number}")
    private int defaultPageNumber;
    @Value("${home-media-app.contextualwebsearch.page-size}")
    private int defaultPageSize;
    @Value("${home-media-app.contextualwebsearch.cache-cleanup-strategy}")
    private CacheCleanupStrategyName cacheCleanupStrategyName;
    private Map<CacheCleanupStrategyName, CleanupStrategy> nameCleanupStrategyMap;

    public ContextualWebSearchConfiguration() {
        nameCleanupStrategyMap = new HashMap<>();
        nameCleanupStrategyMap.put(CacheCleanupStrategyName.OlderThanToday, new OlderThanTodayStrategy());
    }

    @Bean
    public ThumbnailRepository thumbnailRepository(CachedImageRepository cachedImageRepository) {
        CleanupStrategy cleanupStrategy = nameCleanupStrategyMap.get(cacheCleanupStrategyName);
        ImageSearchApi imageSearchApi = new ImageSearchApi(new RestTemplate(), URI.create(baseUri), defaultPageSize, defaultPageNumber);
        ImageCache imageCache = new ImageCache(cachedImageRepository, new CachedImageFactory(), cleanupStrategy);
        return new ImageSearch(imageCache, imageSearchApi);
    }

    public enum CacheCleanupStrategyName {
        OlderThanToday
    }
}
