package com.gorolykmaxim.homemediaapp.model.tvshow;

import com.gorolykmaxim.homemediaapp.model.tvshow.episode.EpisodeFactory;
import com.gorolykmaxim.homemediaapp.model.tvshow.episode.EpisodeRepository;
import com.gorolykmaxim.homemediaapp.model.tvshow.episode.EpisodeStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class TvShowConfiguration {
    @Value("${home-media-app.tv-show.default-thumbnail}")
    private String defaultThumbnail;

    @Bean
    public EpisodeRepository episodeRepository(EpisodeStorage episodeStorage) {
        return new EpisodeRepository(episodeStorage, new EpisodeFactory());
    }

    @Bean
    public TvShowFactory tvShowFactory() {
        return new TvShowFactory(URI.create(defaultThumbnail));
    }

    @Bean
    public TvShowRepository tvShowRepository(TvShowStorage storage, TvShowDao dao) {
        return new TvShowRepository(storage, dao);
    }
}
