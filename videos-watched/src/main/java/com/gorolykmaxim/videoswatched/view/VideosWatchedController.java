package com.gorolykmaxim.videoswatched.view;

import com.gorolykmaxim.videoswatched.domain.notification.NotificationRepository;
import com.gorolykmaxim.videoswatched.domain.video.VideoRepository;
import com.gorolykmaxim.videoswatched.domain.video.VideoThumbnailService;
import com.gorolykmaxim.videoswatched.readmodel.VideoGroupDoesNotExistException;
import com.gorolykmaxim.videoswatched.readmodel.VideoGroupReadModel;
import com.gorolykmaxim.videoswatched.readmodel.VideoGroupReadModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private VideoThumbnailService thumbnailService;
    private VideoRepository videoRepository;
    private NotificationRepository notificationRepository;

    @Autowired
    public VideosWatchedController(VideoGroupReadModelRepository videoGroupRepository, VideoThumbnailService thumbnailService,
                                   VideoRepository videoRepository, NotificationRepository notificationRepository) {
        this.videoGroupRepository = videoGroupRepository;
        this.thumbnailService = thumbnailService;
        this.videoRepository = videoRepository;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping
    public ModelAndView showLatestWatchedVideos() {
        try {
            ModelAndView modelAndView = new ModelAndView("latest-watched-videos");
            modelAndView.addObject("notifications", notificationRepository.findAll());
            modelAndView.addObject("groups", videoGroupRepository.findAll());
            return modelAndView;
        } catch (Exception e) {
            throw ViewException.showWatchedVideoGroups(e);
        }
    }

    @GetMapping("{groupId}")
    public ModelAndView showGroupWatchHistory(@PathVariable int groupId) {
        try {
            VideoGroupReadModel group = videoGroupRepository.findGroupById(groupId).orElseThrow(() -> new VideoGroupDoesNotExistException(groupId));
            ModelAndView modelAndView = new ModelAndView("group-watch-history");
            modelAndView.addObject("notifications", notificationRepository.findAll());
            modelAndView.addObject("group", group);
            return modelAndView;
        } catch (Exception e) {
            throw ViewException.showVideoGroupWatchHistory(groupId, e);
        }
    }

    @GetMapping("clear-watch-history-for-group/{groupId}")
    public ModelAndView confirmWatchHistoryClear(@PathVariable int groupId) {
        try {
            VideoGroupReadModel group = videoGroupRepository.findGroupById(groupId).orElseThrow(() -> new VideoGroupDoesNotExistException(groupId));
            ModelAndView modelAndView = new ModelAndView("confirm-watch-history-clear");
            modelAndView.addObject("groupName", group.getName());
            return modelAndView;
        } catch (Exception e) {
            throw ViewException.clearWatchHistoryForGroup(groupId, e);
        }
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

    @PostMapping("clear-watch-history-for-group/{groupId}")
    public String clearWatchHistoryForGroup(@PathVariable int groupId) {
        try {
            VideoGroupReadModel group = videoGroupRepository.findGroupById(groupId).orElseThrow(() -> new VideoGroupDoesNotExistException(groupId));
            videoRepository.deleteAllByRelativePathStartingWith(group.getName());
            return "redirect:/";
        } catch (Exception e) {
            throw ViewException.clearWatchHistoryForGroup(groupId, e);
        }
    }

    @GetMapping("thumbnail/{videoId}")
    public ResponseEntity<?> downloadThumbnail(@PathVariable String videoId) {
        return thumbnailService.downloadThumbnail(videoId);
    }
}
