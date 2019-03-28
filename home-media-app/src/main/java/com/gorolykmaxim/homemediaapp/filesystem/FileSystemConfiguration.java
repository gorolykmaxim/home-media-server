package com.gorolykmaxim.homemediaapp.filesystem;

import com.gorolykmaxim.homemediaapp.model.tvshow.EpisodeFactory;
import com.gorolykmaxim.homemediaapp.model.tvshow.EpisodeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileSystemConfiguration {
    @Bean
    public EpisodeRepository episodeRepository(EpisodeFactory episodeFactory) {
        return new FileSystemEpisodeRepository(episodeFactory);
    }
}
