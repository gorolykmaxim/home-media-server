package com.gorolykmaxim.torrentui.model;

public interface DownloadStrategy {
    void downloadVia(TorrentService service);
}
