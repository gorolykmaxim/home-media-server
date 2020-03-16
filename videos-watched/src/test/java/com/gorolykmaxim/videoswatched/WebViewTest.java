package com.gorolykmaxim.videoswatched;

import com.gorolykmaxim.videoswatched.domain.notification.Notification;
import com.gorolykmaxim.videoswatched.domain.notification.NotificationRepository;
import com.gorolykmaxim.videoswatched.domain.video.Video;
import com.gorolykmaxim.videoswatched.domain.video.VideoFileService;
import com.gorolykmaxim.videoswatched.domain.video.VideoRepository;
import com.gorolykmaxim.videoswatched.domain.video.VideoThumbnailService;
import com.gorolykmaxim.videoswatched.readmodel.VideoGroupReadModel;
import com.gorolykmaxim.videoswatched.readmodel.VideoGroupReadModelRepository;
import com.gorolykmaxim.videoswatched.readmodel.VideoReadModel;
import com.gorolykmaxim.videoswatched.view.VideosWatchedController;
import com.gorolykmaxim.videoswatched.view.ViewException;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.Paths;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class WebViewTest {
    private VideoGroupReadModel expectedGroup;
    private VideoRepository videoRepository;
    private VideoFileService fileService;
    private VideoThumbnailService thumbnailService;
    private String durationFormat;
    private Clock clock;
    private NotificationRepository notificationRepository;
    private VideoGroupReadModelRepository videoGroupReadModelRepository;
    private VideosWatchedController controller;

    @Before
    public void setUp() throws Exception {
        durationFormat = "m 'minutes' s 'seconds'";
        clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        videoRepository = mock(VideoRepository.class);
        notificationRepository = mock(NotificationRepository.class);
        fileService = mock(VideoFileService.class);
        thumbnailService = mock(VideoThumbnailService.class);
        when(videoRepository.findAll()).thenReturn(Collections.emptyList());
        when(notificationRepository.findAll()).thenReturn(Collections.emptyList());
        videoGroupReadModelRepository = new VideoGroupReadModelRepository(videoRepository, durationFormat, clock);
        controller = new VideosWatchedController(videoGroupReadModelRepository, thumbnailService, videoRepository, notificationRepository);
        generateVideoWatchHistory();
        expectedGroup = getExpectedGroup();
    }

    @Test
    public void shouldShowLatestWatchedVideosThatHaveBeenCompletelyInitializedInTheirDescendingLastPlayOrder() {
        // when
        ModelAndView modelAndView = controller.showLatestWatchedVideos();
        // then
        assertEquals("latest-watched-videos", modelAndView.getViewName());
        Iterable<VideoGroupReadModel> videoGroups = (List<VideoGroupReadModel>) modelAndView.getModel().get("groups");
        List<VideoReadModel> videoReadModels = new ArrayList<>();
        videoGroups.forEach(videoGroupReadModel -> videoGroupReadModel.getVideos().forEach(videoReadModels::add));
        int i = 0;
        for (VideoReadModel video: videoReadModels) {
            i++;
            assertEquals("Video " + i, video.getName());
            assertEquals(DurationFormatUtils.formatDuration(i * 100, durationFormat, false), video.getTimePlayed());
            assertEquals(DurationFormatUtils.formatDuration(i * 10000, durationFormat, false), video.getTotalTime());
            assertEquals(i == 1 ? "Today" : String.format("%d days ago", i - 1), video.getLastPlayedDate());
        }
        assertEquals(3, i);
    }

    @Test
    public void shouldShowAllTheAvailableNotificationsAlongTheVideoGroups() {
        // given
        when(notificationRepository.findAll()).thenReturn(Arrays.asList(
                Notification.createNew("Notification 1"),
                Notification.createNew("Notification 2"),
                Notification.createNew("Notification 3")
        ));
        // when
        ModelAndView modelAndView = controller.showLatestWatchedVideos();
        // then
        Iterable<Notification> notifications = (Iterable<Notification>) modelAndView.getModel().get("notifications");
        int i = 1;
        for (Notification notification: notifications) {
            assertEquals("Notification " + i++, notification.getContent());
        }
    }

    @Test
    public void shouldShowNoNotifications() {
        // when
        ModelAndView modelAndView = controller.showLatestWatchedVideos();
        // then
        Iterable<Notification> notifications = (Iterable<Notification>) modelAndView.getModel().get("notifications");
        assertEquals(0, StreamSupport.stream(notifications.spliterator(), false).count());
    }

    @Test(expected = ViewException.class)
    public void shouldFailToShowGroupsOfWatchedVideosAndThrowAnException() {
        // given
        when(videoRepository.findAll()).thenThrow(new RuntimeException());
        // when
        controller.showLatestWatchedVideos();
    }

    @Test
    public void shouldShowWatchHistoryOfTheSpecifiedVideoGroup() {
        // when
        ModelAndView modelAndView = controller.showGroupWatchHistory(expectedGroup.getId());
        // then
        assertEquals("group-watch-history", modelAndView.getViewName());
        VideoGroupReadModel group = (VideoGroupReadModel) modelAndView.getModel().get("group");
        assertEquals(expectedGroup.getId(), group.getId());
        assertEquals(expectedGroup.getName(), group.getName());
        assertEquals(expectedGroup.getVideos(), group.getVideos());
    }

    @Test
    public void shouldShowAllTheAvailableNotificationsAlongTheVideoGroupHistory() {
        // given
        when(notificationRepository.findAll()).thenReturn(Arrays.asList(
                Notification.createNew("Notification 1"),
                Notification.createNew("Notification 2"),
                Notification.createNew("Notification 3")
        ));
        // when
        ModelAndView modelAndView = controller.showGroupWatchHistory(expectedGroup.getId());
        // then
        Iterable<Notification> notifications = (Iterable<Notification>) modelAndView.getModel().get("notifications");
        int i = 1;
        for (Notification notification: notifications) {
            assertEquals("Notification " + i++, notification.getContent());
        }
    }

    @Test
    public void shouldShowNoNotificationsAlongTheVideoGroupHistory() {
        // when
        ModelAndView modelAndView = controller.showGroupWatchHistory(expectedGroup.getId());
        // then
        Iterable<Notification> notifications = (Iterable<Notification>) modelAndView.getModel().get("notifications");
        assertEquals(0, StreamSupport.stream(notifications.spliterator(), false).count());
    }

    @Test(expected = ViewException.class)
    public void shouldFailToShowWatchHistoryOfTheSpecifiedVideoGroupAndThrowAnException() {
        // given
        when(videoRepository.findAll()).thenThrow(new RuntimeException());
        // when
        controller.showGroupWatchHistory(expectedGroup.getId());
    }

    @Test(expected = ViewException.class)
    public void shouldFailToShowWatchHistoryOfAGroupThatDoesNotExist() {
        // when
        controller.showGroupWatchHistory(1);
    }

    @Test
    public void shouldDirectUserToClearWatchHistoryConfirmationDialog() {
        // when
        ModelAndView modelAndView = controller.confirmWatchHistoryClear(expectedGroup.getId());
        // then
        assertEquals("confirm-watch-history-clear", modelAndView.getViewName());
        String groupName = (String) modelAndView.getModel().get("groupName");
        assertEquals(expectedGroup.getName(), groupName);
    }

    @Test(expected = ViewException.class)
    public void shouldFailToDirectUserToClearWatchHistoryConfirmationDialogSinceSpecifiedGroupDoesNotExist() {
        // when
        controller.confirmWatchHistoryClear(1);
    }

    @Test
    public void shouldDiscardAllNotificationsAndRedirectBackToTheRootPage() {
        // when
        String url = controller.discardNotifications();
        // then
        assertEquals("redirect:/", url);
        verify(notificationRepository).deleteAll();
    }

    @Test(expected = ViewException.class)
    public void shouldFailToDiscardAllNotificationsAndThrowAnException() {
        // given
        doThrow(new RuntimeException()).when(notificationRepository).deleteAll();
        // when
        controller.discardNotifications();
    }

    @Test
    public void shouldClearWatchHistoryForTheSpecifiedGroupAndRedirectBackToTheRootPAge() {
        // when
        String url = controller.clearWatchHistoryForGroup(expectedGroup.getId());
        // then
        assertEquals("redirect:/", url);
        verify(videoRepository).deleteAllByRelativePathStartingWith(expectedGroup.getName());
    }

    @Test(expected = ViewException.class)
    public void shouldFailToClearWatchHistoryForTheSpecifiedGroupAndThrowAnException() {
        // given
        doThrow(new RuntimeException()).when(videoRepository).deleteAllByRelativePathStartingWith(expectedGroup.getName());
        // when
        controller.clearWatchHistoryForGroup(expectedGroup.getId());
    }

    @Test(expected = ViewException.class)
    public void shouldFailToClearWatchHistoryForTheGroupThatDoesNotExist() {
        // when
        controller.clearWatchHistoryForGroup(1);
    }

    private void generateVideoWatchHistory() {
        List<Video> videos = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 1; i <= 4; i++) {
            Video video = new Video(i);
            video.setName("Video " + i);
            if (i < 4) {
                video.setTimePlayed(i * 100);
                video.setTotalTime(i * 10000);
            }
            video.setLastPlayDate(now.minusDays(i - 1));
            int groupNumber = i <= 2 ? 1 : 2;
            when(fileService.resolvePathToVideoFile(video.getName())).thenReturn(Paths.get("Group " + groupNumber, "Folder"));
            video.updateRelativePathIfNecessary(fileService);
            videos.add(video);
        }
        when(videoRepository.findAll()).thenReturn(videos);
    }

    private VideoGroupReadModel getExpectedGroup() {
        return StreamSupport.stream(videoGroupReadModelRepository.findAll().spliterator(), false)
                .filter(group -> group.getName().equals("Group 2"))
                .findFirst()
                .get();
    }
}
