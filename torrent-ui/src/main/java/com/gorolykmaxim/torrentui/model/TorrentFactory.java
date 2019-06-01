package com.gorolykmaxim.torrentui.model;

public class TorrentFactory {

    public Torrent createMagnet(String magnetLink, String downloadFolder) {
        if (magnetLink == null || magnetLink.isEmpty()) {
            throw new IllegalArgumentException("Magnet link should not be empty");
        }
        if (downloadFolder == null || downloadFolder.isEmpty()) {
            throw new IllegalArgumentException("Download folder should not be empty");
        }
        return new Torrent(new MagnetLinkStrategy(magnetLink, downloadFolder));
    }

}
