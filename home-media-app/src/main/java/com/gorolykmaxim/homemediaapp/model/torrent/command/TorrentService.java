package com.gorolykmaxim.homemediaapp.model.torrent.command;

public interface TorrentService {
    void downloadViaMagnetLink(String magnetLink, String downloadFolder);
    void deleteTorrentById(String id);
}
