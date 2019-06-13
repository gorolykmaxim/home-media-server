package com.gorolykmaxim.thumbnailsearch;

import com.gorolykmaxim.thumbnailsearch.contextualwebsearch.ImageSearch;
import com.gorolykmaxim.thumbnailsearch.contextualwebsearch.api.ImageSearchApi;
import com.gorolykmaxim.thumbnailsearch.contextualwebsearch.cache.*;
import com.gorolykmaxim.thumbnailsearch.model.ThumbnailRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@Configuration
public class ThumbnailSearchApplication {
    @Value("${thumbnail-search.contextualwebsearch.base-uri}")
    private String baseUri;
    @Value("${thumbnail-search.contextualwebsearch.page-number}")
    private int defaultPageNumber;
    @Value("${thumbnail-search.contextualwebsearch.page-size}")
    private int defaultPageSize;
    @Value("${thumbnail-search.contextualwebsearch.cache-cleanup-strategy}")
    private CacheCleanupStrategyName cacheCleanupStrategyName;
    private Map<CacheCleanupStrategyName, CleanupStrategy> nameCleanupStrategyMap;

    public ThumbnailSearchApplication() {
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

    public static void main(String[] args) {
        SpringApplication.run(ThumbnailSearchApplication.class, args);
    }
}
