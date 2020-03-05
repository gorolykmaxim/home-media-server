package com.gorolykmaxim.videoswatched.view;

import com.gorolykmaxim.videoswatched.domain.notification.NotificationRepository;
import com.gorolykmaxim.videoswatched.domain.video.VideoRepository;
import com.gorolykmaxim.videoswatched.readmodel.VideoGroupReadModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class VideosWatchedController {
    private VideoGroupReadModelRepository videoGroupRepository;
    private VideoRepository videoRepository;
    private NotificationRepository notificationRepository;

    @Autowired
    public VideosWatchedController(VideoGroupReadModelRepository videoGroupRepository, VideoRepository videoRepository,
                                   NotificationRepository notificationRepository) {
        this.videoGroupRepository = videoGroupRepository;
        this.videoRepository = videoRepository;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping
    public ModelAndView showWatchedVideoGroups() {
        try {
            ModelAndView modelAndView = new ModelAndView("watched-video-groups");
            modelAndView.addObject("notifications", notificationRepository.findAll());
            modelAndView.addObject("groups", videoGroupRepository.findAll());
            return modelAndView;
        } catch (Exception e) {
            throw ViewException.showWatchedVideoGroups(e);
        }
    }

    @GetMapping("clear-watch-history-for-group/{groupName}")
    public ModelAndView confirmWatchHistoryClear(@PathVariable String groupName) {
        ModelAndView modelAndView = new ModelAndView("confirm-watch-history-clear");
        modelAndView.addObject("groupName", groupName);
        return modelAndView;
    }

    @PostMapping("discard-notifications")
    public String discardNotifications() {
        try {
            notificationRepository.deleteAll();
            return "redirect:/";
        } catch (Exception e) {
            throw ViewException.discardNotifications(e);
        }
    }

    @PostMapping("clear-watch-history-for-group/{groupName}")
    public String clearWatchHistoryForGroup(@PathVariable String groupName) {
        try {
            videoRepository.deleteAllByRelativePathStartingWith(groupName);
            return "redirect:/";
        } catch (Exception e) {
            throw ViewException.clearWatchHistoryForGroup(groupName, e);
        }
    }
}
