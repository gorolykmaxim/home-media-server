package com.gorolykmaxim.torrentui;

import com.gorolykmaxim.torrentui.common.DownloadSpeedFormatter;
import com.gorolykmaxim.torrentui.common.DurationFormatter;
import com.gorolykmaxim.torrentui.common.SizeFormatter;
import com.gorolykmaxim.torrentui.common.keyvalue.KeyValueRepository;
import com.gorolykmaxim.torrentui.model.DownloadingTorrentRepository;
import com.gorolykmaxim.torrentui.model.TorrentFactory;
import com.gorolykmaxim.torrentui.model.TorrentRepository;
import com.gorolykmaxim.torrentui.model.TorrentService;
import com.gorolykmaxim.torrentui.qbittorrent.QbittorrentAuthorization;
import com.gorolykmaxim.torrentui.qbittorrent.QbittorrentFactory;
import com.gorolykmaxim.torrentui.qbittorrent.QbittorrentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.text.NumberFormat;
import java.util.Locale;

@SpringBootApplication
@Configuration
public class TorrentUiApplication {
    @Value("${torrent-ui.qbittorrent.base-uri}")
    private String baseUri;
    @Value("${torrent-ui.qbittorrent.username}")
    private String userName;
    @Value("${torrent-ui.qbittorrent.password}")
    private String password;
    @Value("${torrent-ui.torrent.sort}")
    private String sort;
    @Value("${torrent-ui.torrent.reverse}")
    private boolean reverse;
    @Value("${torrent-ui.common.duration-format}")
    private String durationFormat;
    @Value("${torrent-ui.common.download-speed-format}")
    private String downloadSpeedFormat;

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

    @Bean
    public TorrentService torrentService(KeyValueRepository keyValueRepository) {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = URI.create(baseUri);
        SizeFormatter sizeFormatter = new SizeFormatter();
        DurationFormatter durationFormatter = new DurationFormatter(durationFormat, false);
        DownloadSpeedFormatter downloadSpeedFormatter = new DownloadSpeedFormatter(downloadSpeedFormat, sizeFormatter);
        QbittorrentAuthorization authorization = new QbittorrentAuthorization(restTemplate, userName, password, uri,
                keyValueRepository);
        QbittorrentFactory factory = new QbittorrentFactory(sizeFormatter, durationFormatter, downloadSpeedFormatter);
        return new QbittorrentService(restTemplate, authorization, factory, uri);
    }

    public static void main(String[] args) {
        SpringApplication.run(TorrentUiApplication.class, args);
    }
}
