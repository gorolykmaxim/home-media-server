package com.gorolykmaxim.homemediaapp.model.torrent.command;

public class TorrentRepository {

    private TorrentService service;

    public TorrentRepository(TorrentService service) {
        this.service = service;
    }

    public void save(Torrent torrent) {
        torrent.downloadVia(service);
    }

    public void deleteById(String id) {
        service.deleteTorrentById(id);
    }

}
