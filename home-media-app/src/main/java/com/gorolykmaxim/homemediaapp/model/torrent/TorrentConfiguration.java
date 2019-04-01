package com.gorolykmaxim.homemediaapp.model.torrent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TorrentConfiguration {
    @Value("${home-media-app.torrent.sort}")
    private String sort;
    @Value("${home-media-app.torrent.reverse}")
    private boolean reverse;

    @Bean
    public TorrentFactory torrentFactory() {
        return new TorrentFactory();
    }

    @Bean
    public TorrentRepository torrentRepository(TorrentService torrentService) {
        return new TorrentRepository(torrentService);
    }

    @Bean
    public DownloadingTorrentRepository downloadingTorrentRepository(TorrentService torrentService) {
        return new DownloadingTorrentRepository(torrentService, sort, reverse);
    }

}
