package com.gorolykmaxim.homemediaapp.contextualwebsearch;

import com.gorolykmaxim.homemediaapp.contextualwebsearch.api.ImageSearchApi;
import com.gorolykmaxim.homemediaapp.contextualwebsearch.cache.CachedImageFactory;
import com.gorolykmaxim.homemediaapp.contextualwebsearch.cache.ImageCache;
import com.gorolykmaxim.homemediaapp.contextualwebsearch.cache.CachedImageRepository;
import com.gorolykmaxim.homemediaapp.contextualwebsearch.cache.OlderThanTodayStrategy;
import com.gorolykmaxim.homemediaapp.model.tvshow.ThumbnailRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Configuration
public class ContextualWebSearchConfiguration {

    @Value("${home-media-app.contextualwebsearch.base-uri}")
    private String baseUri;

    @Bean
    public ThumbnailRepository thumbnailRepository(CachedImageRepository cachedImageRepository) {
        ImageSearchApi imageSearchApi = new ImageSearchApi(new RestTemplate(), URI.create(baseUri));
        ImageCache imageCache = new ImageCache(cachedImageRepository, new CachedImageFactory(), new OlderThanTodayStrategy());
        return new ImageSearch(imageCache, imageSearchApi);
    }
}
