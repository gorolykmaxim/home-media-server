package com.gorolykmaxim.homemediaapp.service.view.torrent;

import com.gorolykmaxim.homemediaapp.model.torrent.DownloadingTorrentRepository;
import com.gorolykmaxim.homemediaapp.model.torrent.Torrent;
import com.gorolykmaxim.homemediaapp.model.torrent.TorrentFactory;
import com.gorolykmaxim.homemediaapp.model.torrent.TorrentRepository;
import com.gorolykmaxim.homemediaapp.service.view.ViewError;
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

    @Value("${home-media-app.torrent.default-download-folder}")
    public void setDefaultDownloadFolder(String defaultDownloadFolder) {
        this.defaultDownloadFolder = defaultDownloadFolder;
    }

    @GetMapping
    public ModelAndView showTorrentList() {
        try {
            ModelAndView modelAndView = new ModelAndView("torrent/list");
            modelAndView.addObject("torrentList", downloadingTorrentRepository.findAll());
            modelAndView.addObject("torrentDeleteUrlPrefix", "/torrent/delete");
            return modelAndView;
        } catch (RuntimeException e) {
            throw new ViewError(e);
        }
    }

    @GetMapping("download")
    public ModelAndView showDownloadTorrentForm() {
        ModelAndView modelAndView = new ModelAndView("torrent/new");
        modelAndView.addObject("defaultDownloadFolder", defaultDownloadFolder);
        modelAndView.addObject("submitUrl", "/torrent/download");
        return modelAndView;
    }

    @GetMapping("delete/{id}")
    public String deleteTorrentById(@PathVariable("id") String id) {
        try {
            torrentRepository.deleteById(id);
            return "redirect:/torrent";
        } catch (RuntimeException e) {
            throw new ViewError(e);
        }
    }

    @PostMapping("download")
    public String downloadTorrent(TorrentPrototype prototype) {
        try {
            Torrent torrent = factory.createMagnet(prototype.getMagnetLink(), prototype.getDownloadFolder());
            torrentRepository.save(torrent);
            return "redirect:/torrent";
        } catch (RuntimeException e) {
            throw new ViewError(e);
        }
    }

}
