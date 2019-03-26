package com.gorolykmaxim.homemediaapp.model.torrent;

import com.gorolykmaxim.homemediaapp.model.torrent.command.TorrentFactory;
import com.gorolykmaxim.homemediaapp.model.torrent.command.TorrentRepository;
import com.gorolykmaxim.homemediaapp.model.torrent.command.TorrentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TorrentConfiguration {

    @Bean
    public TorrentFactory torrentFactory() {
        return new TorrentFactory();
    }

    @Bean
    public TorrentRepository torrentRepository(TorrentService torrentService) {
        return new TorrentRepository(torrentService);
    }

}
