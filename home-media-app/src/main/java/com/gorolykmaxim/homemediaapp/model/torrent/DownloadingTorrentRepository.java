package com.gorolykmaxim.homemediaapp.model.torrent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadingTorrentRepository {
    private TorrentService service;

    public DownloadingTorrentRepository(TorrentService service) {
        this.service = service;
    }

    public List<DownloadingTorrent> findAll() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("sort", "progress");
        return service.find(parameters);
    }
}
