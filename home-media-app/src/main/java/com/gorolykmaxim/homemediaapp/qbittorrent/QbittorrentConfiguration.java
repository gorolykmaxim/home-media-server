package com.gorolykmaxim.homemediaapp.qbittorrent;

import com.gorolykmaxim.homemediaapp.common.DownloadSpeedFormatter;
import com.gorolykmaxim.homemediaapp.common.DurationFormatter;
import com.gorolykmaxim.homemediaapp.common.SizeFormatter;
import com.gorolykmaxim.homemediaapp.common.keyvalue.KeyValueRepository;
import com.gorolykmaxim.homemediaapp.model.torrent.command.TorrentService;
import com.gorolykmaxim.homemediaapp.model.torrent.query.DownloadingTorrentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.text.NumberFormat;

@Configuration
public class QbittorrentConfiguration {

    @Value("${home-media-app.qbittorrent.base-uri}")
    private String baseUri;
    @Value("${home-media-app.qbittorrent.username}")
    private String userName;
    @Value("${home-media-app.qbittorrent.password}")
    private String password;

    @Bean
    public DownloadingTorrentRepository downloadingTorrentRepository(KeyValueRepository keyValueRepository,
                                                                     SizeFormatter sizeFormatter,
                                                                     DurationFormatter durationFormatter,
                                                                     DownloadSpeedFormatter downloadSpeedFormatter,
                                                                     NumberFormat percentFormat) {
        return qbittorrentService(keyValueRepository, sizeFormatter, durationFormatter, downloadSpeedFormatter,
                percentFormat);
    }

    @Bean
    public TorrentService torrentService(KeyValueRepository keyValueRepository, SizeFormatter sizeFormatter,
                                         DurationFormatter durationFormatter,
                                         DownloadSpeedFormatter downloadSpeedFormatter, NumberFormat percentFormat) {
        return qbittorrentService(keyValueRepository, sizeFormatter, durationFormatter, downloadSpeedFormatter,
                percentFormat);
    }

    public QbittorrentService qbittorrentService(KeyValueRepository keyValueRepository,
                                                 SizeFormatter sizeFormatter, DurationFormatter durationFormatter,
                                                 DownloadSpeedFormatter downloadSpeedFormatter,
                                                 NumberFormat percentFormat) {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = URI.create(baseUri);
        QbittorrentAuthorization authorization = new QbittorrentAuthorization(restTemplate, userName, password, uri,
                keyValueRepository);
        QbittorrentFactory factory = new QbittorrentFactory(sizeFormatter, durationFormatter, downloadSpeedFormatter,
                percentFormat);
        return new QbittorrentService(restTemplate, authorization, factory, uri);
    }

}
