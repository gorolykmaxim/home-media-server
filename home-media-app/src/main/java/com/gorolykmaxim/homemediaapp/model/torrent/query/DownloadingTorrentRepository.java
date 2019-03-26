package com.gorolykmaxim.homemediaapp.model.torrent.query;

import java.util.List;

public interface DownloadingTorrentRepository {
    List<DownloadingTorrent> findAll();
}
