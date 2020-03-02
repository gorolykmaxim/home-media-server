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
    private long refreshInterval;
    private TorrentFactory factory;
    private TorrentRepository torrentRepository;
    private DownloadingTorrentRepository downloadingTorrentRepository;
    private ErrorControllerAdvice errorControllerAdvice;

    @Autowired
    public TorrentController(TorrentFactory factory, TorrentRepository torrentRepository, DownloadingTorrentRepository downloadingTorrentRepository) {
        this.factory = factory;
        this.torrentRepository = torrentRepository;
        this.downloadingTorrentRepository = downloadingTorrentRepository;
        errorControllerAdvice = new ErrorControllerAdvice();
    }

    @Value("${torrent-ui.torrent.default-download-folder}")
    public void setDefaultDownloadFolder(String defaultDownloadFolder) {
        this.defaultDownloadFolder = defaultDownloadFolder;
    }

    @Value("${torrent-ui.torrent.refresh-interval}")
    public void setRefreshInterval(long refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    @GetMapping
    public ModelAndView showTorrentList() {
        ModelAndView modelAndView = new ModelAndView("list");
        modelAndView.addObject("refreshInterval", refreshInterval);
        modelAndView.addObject("downloadTorrentFormUrl", "/download");
        return modelAndView;
    }

    @GetMapping("list")
    public ModelAndView renderTorrentList() {
        try {
            ModelAndView modelAndView = new ModelAndView("torrent-list");
            modelAndView.addObject("torrents", downloadingTorrentRepository.findAll());
            modelAndView.addObject("deleteUrlPrefix", "/delete");
            return modelAndView;
        } catch (RuntimeException e) {
            ModelAndView modelAndView = new ModelAndView("error-component");
            errorControllerAdvice.applyErrorInformationTo(modelAndView, new ViewError(e));
            return modelAndView;
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
            modelAndView.addObject("deleteUrl", "/delete");
            modelAndView.addObject("cancelUrl", "/");
            return modelAndView;
        } catch (RuntimeException e) {
            throw new ViewError(e);
        }
    }

    @PostMapping("delete")
    public String deleteTorrentById(DeleteTorrent deleteTorrent) {
        try {
            torrentRepository.deleteById(deleteTorrent.getId(), deleteTorrent.isDeleteData());
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
