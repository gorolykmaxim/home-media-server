package com.gorolykmaxim.homemediaapp.model.torrent;

public interface DownloadStrategy {
    void downloadVia(TorrentService service);
}
