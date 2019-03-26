package com.gorolykmaxim.homemediaapp.service.view.torrent;

import com.gorolykmaxim.homemediaapp.model.torrent.command.Torrent;
import com.gorolykmaxim.homemediaapp.model.torrent.command.TorrentFactory;
import com.gorolykmaxim.homemediaapp.model.torrent.command.TorrentRepository;
import com.gorolykmaxim.homemediaapp.model.torrent.query.DownloadingTorrentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("torrent")
public class TorrentController {

    private String defaultDownloadFolder;
    private TorrentFactory factory;
    private TorrentRepository torrentRepository;
    private DownloadingTorrentRepository downloadingTorrentRepository;

    @Autowired
    public TorrentController(TorrentFactory factory, TorrentRepository torrentRepository, DownloadingTorrentRepository downloadingTorrentRepository) {
        this.factory = factory;
        this.torrentRepository = torrentRepository;
        this.downloadingTorrentRepository = downloadingTorrentRepository;
    }

    @Value("${home-media-app.torrent.default-download-folder:/downloads/}")
    public void setDefaultDownloadFolder(String defaultDownloadFolder) {
        this.defaultDownloadFolder = defaultDownloadFolder;
    }

    @GetMapping
    public ModelAndView showTorrentList() {
        ModelAndView modelAndView = new ModelAndView("torrent/list");
        modelAndView.addObject("torrentList", downloadingTorrentRepository.findAll());
        return modelAndView;
    }

    @GetMapping("download")
    public ModelAndView showDownloadTorrentForm() {
        ModelAndView modelAndView = new ModelAndView("torrent/new");
        modelAndView.addObject("defaultDownloadFolder", defaultDownloadFolder);
        return modelAndView;
    }

    @GetMapping("delete/{id}")
    public String deleteTorrentById(@PathVariable("id") String id) {
        torrentRepository.deleteById(id);
        return "redirect:/torrent";
    }

    @PostMapping("download")
    public String downloadTorrent(TorrentPrototype prototype) {
        Torrent torrent = factory.createMagnet(prototype.getMagnetLink(), prototype.getDownloadFolder());
        torrentRepository.save(torrent);
        return "redirect:/torrent";
    }

}