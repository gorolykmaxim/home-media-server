package com.gorolykmaxim.homemediaapp.model.tvshow;

import com.gorolykmaxim.homemediaapp.common.PathResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class TvShowConfiguration {
    @Value("${home-media-app.tv-show.directory}")
    private String rootDirectory;
    @Value("${home-media-app.tv-show.default-thumbnail}")
    private String defaultThumbnail;

    @Bean
    public EpisodeFactory episodeFactory() {
        return new EpisodeFactory();
    }

    @Bean
    public TvShowFactory tvShowFactory() {
        return new TvShowFactory(new PathResolver(rootDirectory), URI.create(defaultThumbnail));
    }
}
