package com.gorolykmaxim.homemediaapp.service.view;

import com.gorolykmaxim.homemediaapp.model.torrent.DownloadingTorrentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("downloads")
public class DownloadsController {
    private DownloadingTorrentRepository downloadingTorrentRepository;

    public DownloadsController(DownloadingTorrentRepository downloadingTorrentRepository) {
        this.downloadingTorrentRepository = downloadingTorrentRepository;
    }

    @GetMapping
    public ModelAndView showDownloads() {
        try {
            ModelAndView modelAndView = new ModelAndView("downloads");
            modelAndView.addObject("downloads", downloadingTorrentRepository.findDownloading());
            return modelAndView;
        } catch (RuntimeException e) {
            throw new ViewError(e);
        }
    }
}
