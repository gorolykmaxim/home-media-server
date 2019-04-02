package com.gorolykmaxim.homemediaapp.service.api;

import com.gorolykmaxim.homemediaapp.model.torrent.*;
import com.gorolykmaxim.homemediaapp.service.model.TorrentPrototype;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("apiTorrentController")
@RequestMapping("/api/v1/torrent")
public class TorrentController {

    private TorrentFactory factory;
    private TorrentRepository torrentRepository;
    private DownloadingTorrentRepository downloadingTorrentRepository;

    public TorrentController(TorrentFactory factory, TorrentRepository torrentRepository,
                             DownloadingTorrentRepository downloadingTorrentRepository) {
        this.factory = factory;
        this.torrentRepository = torrentRepository;
        this.downloadingTorrentRepository = downloadingTorrentRepository;
    }

    @GetMapping
    public List<DownloadingTorrent> getAll() {
        return downloadingTorrentRepository.findAll();
    }

    @PostMapping
    public void create(@RequestBody TorrentPrototype prototype) {
        try {
            Torrent torrent = factory.createMagnet(prototype.getMagnetLink(), prototype.getDownloadFolder());
            torrentRepository.save(torrent);
        } catch (IllegalArgumentException e) {
            throw new InvalidTorrentError(prototype, e);
        }
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        torrentRepository.deleteById(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class InvalidTorrentError extends RuntimeException {
        public InvalidTorrentError(TorrentPrototype prototype, Throwable cause) {
            super(String.format("Failed to download torrent '%s'. Reason: %s", prototype, cause.getMessage()), cause);
        }
    }
}
