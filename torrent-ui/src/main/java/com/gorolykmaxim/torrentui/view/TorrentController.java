package com.gorolykmaxim.torrentui.view;

import com.gorolykmaxim.torrentui.model.DownloadingTorrentRepository;
import com.gorolykmaxim.torrentui.model.Torrent;
import com.gorolykmaxim.torrentui.model.TorrentFactory;
import com.gorolykmaxim.torrentui.model.TorrentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
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

    @Value("${torrent-ui.torrent.default-download-folder}")
    public void setDefaultDownloadFolder(String defaultDownloadFolder) {
        this.defaultDownloadFolder = defaultDownloadFolder;
    }

    @GetMapping
    public ModelAndView showTorrentList() {
        try {
            ModelAndView modelAndView = new ModelAndView("list");
            modelAndView.addObject("downloadTorrentFormUrl", "/download");
            modelAndView.addObject("torrentList", downloadingTorrentRepository.findAll());
            modelAndView.addObject("torrentDeleteUrlPrefix", "/delete");
            return modelAndView;
        } catch (RuntimeException e) {
            throw new ViewError(e);
        }
    }

    @GetMapping("download")
    public ModelAndView showDownloadTorrentForm() {
        ModelAndView modelAndView = new ModelAndView("new");
        modelAndView.addObject("defaultDownloadFolder", defaultDownloadFolder);
        modelAndView.addObject("submitUrl", "/download");
        return modelAndView;
    }

    @GetMapping("delete/{id}")
    public ModelAndView showDeleteTorrentPrompt(@PathVariable("id") String id) {
        try {
            ModelAndView modelAndView = new ModelAndView("delete");
            modelAndView.addObject("torrent", downloadingTorrentRepository.findById(id));
            modelAndView.addObject("deleteUrl", String.format("/delete/%s/confirm", id));
            modelAndView.addObject("cancelUrl", "/");
            return modelAndView;
        } catch (RuntimeException e) {
            throw new ViewError(e);
        }
    }

    @GetMapping("delete/{id}/confirm")
    public String deleteTorrentById(@PathVariable("id") String id) {
        try {
            torrentRepository.deleteById(id);
            return "redirect:/";
        } catch (RuntimeException e) {
            throw new ViewError(e);
        }
    }

    @PostMapping("download")
    public String downloadTorrent(TorrentPrototype prototype) {
        try {
            Torrent torrent = factory.createMagnet(prototype.getMagnetLink(), prototype.getDownloadFolder());
            torrentRepository.save(torrent);
            return "redirect:/";
        } catch (RuntimeException e) {
            throw new ViewError(e);
        }
    }

}
