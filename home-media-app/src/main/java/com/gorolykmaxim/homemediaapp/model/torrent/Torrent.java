package com.gorolykmaxim.homemediaapp.model.torrent;

import java.util.Objects;

public class Torrent {
    private final DownloadStrategy downloadStrategy;

    Torrent(DownloadStrategy downloadStrategy) {
        this.downloadStrategy = downloadStrategy;
    }

    public void downloadVia(TorrentService service) {
        downloadStrategy.downloadVia(service);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Torrent torrent = (Torrent) o;
        return Objects.equals(downloadStrategy, torrent.downloadStrategy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(downloadStrategy);
    }
}
