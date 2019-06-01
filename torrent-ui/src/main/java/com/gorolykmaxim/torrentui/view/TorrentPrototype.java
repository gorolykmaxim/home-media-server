package com.gorolykmaxim.torrentui.view;

import java.util.Objects;

public class TorrentPrototype {
    private String magnetLink, downloadFolder;

    public String getMagnetLink() {
        return magnetLink;
    }

    public void setMagnetLink(String magnetLink) {
        this.magnetLink = magnetLink;
    }

    public String getDownloadFolder() {
        return downloadFolder;
    }

    public void setDownloadFolder(String downloadFolder) {
        this.downloadFolder = downloadFolder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TorrentPrototype that = (TorrentPrototype) o;
        return Objects.equals(magnetLink, that.magnetLink) &&
                Objects.equals(downloadFolder, that.downloadFolder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(magnetLink, downloadFolder);
    }
}
