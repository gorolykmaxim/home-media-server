package com.gorolykmaxim.homemediaapp.service.api;

import com.gorolykmaxim.homemediaapp.model.torrent.DownloadingTorrent;
import com.gorolykmaxim.homemediaapp.model.torrent.DownloadingTorrentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("apiDownloadsController")
@RequestMapping("/api/v1/downloads")
public class DownloadsController {
    private DownloadingTorrentRepository downloadingTorrentRepository;

    public DownloadsController(DownloadingTorrentRepository downloadingTorrentRepository) {
        this.downloadingTorrentRepository = downloadingTorrentRepository;
    }

    @GetMapping
    public List<DownloadingTorrent> getAll() {
        return downloadingTorrentRepository.findDownloading();
    }
}
