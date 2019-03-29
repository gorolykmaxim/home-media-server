package com.gorolykmaxim.homemediaapp.filesystem;

import com.gorolykmaxim.homemediaapp.common.PathResolver;
import com.gorolykmaxim.homemediaapp.model.tvshow.episode.EpisodeStorage;
import com.gorolykmaxim.homemediaapp.model.tvshow.TvShowStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileSystemConfiguration {
    @Value("${home-media-app.tv-show.directory:/shows/}")
    private String rootDirectory;

    public FileSystemStorage fileSystemStorage() {
        return new FileSystemStorage(new PathResolver(rootDirectory));
    }

    @Bean
    public EpisodeStorage episodeStorage() {
        return fileSystemStorage();
    }

    @Bean
    public TvShowStorage tvShowStorage() {
        return fileSystemStorage();
    }
}
