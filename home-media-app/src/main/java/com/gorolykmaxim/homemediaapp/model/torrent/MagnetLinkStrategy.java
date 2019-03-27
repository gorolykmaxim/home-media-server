package com.gorolykmaxim.homemediaapp.model.torrent;

import java.util.Objects;

public class MagnetLinkStrategy implements DownloadStrategy {

    private final String magnetLink, downloadFolder;

    public MagnetLinkStrategy(String magnetLink, String downloadFolder) {
        this.magnetLink = magnetLink;
        this.downloadFolder = downloadFolder;
    }

    @Override
    public void downloadVia(TorrentService service) {
        service.downloadViaMagnetLink(magnetLink, downloadFolder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MagnetLinkStrategy that = (MagnetLinkStrategy) o;
        return Objects.equals(magnetLink, that.magnetLink) &&
                Objects.equals(downloadFolder, that.downloadFolder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(magnetLink, downloadFolder);
    }
}
