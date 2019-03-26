package com.gorolykmaxim.homemediaapp.model.torrent.command;

public interface DownloadStrategy {
    void downloadVia(TorrentService service);
}
