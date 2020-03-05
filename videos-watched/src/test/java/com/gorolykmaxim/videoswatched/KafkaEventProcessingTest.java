package com.gorolykmaxim.videoswatched;

import com.gorolykmaxim.videoswatched.domain.notification.Notification;
import com.gorolykmaxim.videoswatched.domain.notification.NotificationRepository;
import com.gorolykmaxim.videoswatched.domain.video.Video;
import com.gorolykmaxim.videoswatched.domain.video.VideoFileService;
import com.gorolykmaxim.videoswatched.domain.video.VideoRepository;
import com.gorolykmaxim.videoswatched.infrastructure.kafka.EventProcessingException;
import com.gorolykmaxim.videoswatched.infrastructure.kafka.KafkaEventProcessor;
import com.gorolykmaxim.videoswatched.infrastructure.kafka.KafkaVideoEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class KafkaEventProcessingTest {
    private VideoRepository videoRepository;
    private NotificationRepository notificationRepository;
    private VideoFileService service;
    private KafkaEventProcessor processor;
    private KafkaVideoEvent event;
    private Video video;
    private ArgumentCaptor<Video> newVideoCaptor;
    private ArgumentCaptor<Notification> notificationCaptor;

    @Before
    public void setUp() throws Exception {
        Logger logger = LoggerFactory.getLogger(KafkaEventProcessingTest.class);
        event = new KafkaVideoEvent();
        event.setVideoId(21L);
        event.setTimePlayed(150000L);
        event.setTotalTime(1000000L);
        event.setTimestamp("2020-03-03T19:05:14.431Z");
        event.setVideoName("'The Office S01E03'");
        video = new Video(event.getVideoId());
        videoRepository = mock(VideoRepository.class);
        notificationRepository = mock(NotificationRepository.class);
        service = mock(VideoFileService.class);
        newVideoCaptor = ArgumentCaptor.forClass(Video.class);
        notificationCaptor = ArgumentCaptor.forClass(Notification.class);
        when(videoRepository.findById(event.getVideoId())).thenReturn(Optional.of(video));
        when(service.resolvePathToVideoFile(event.getVideoName())).thenReturn(Paths.get("The Office"));
        processor = new KafkaEventProcessor(videoRepository, notificationRepository, service, logger);
    }

    @Test
    public void shouldCreateNewVideoOnEitherAProgressOrATimelineEvent() {
        for (KafkaVideoEvent.Type type: KafkaVideoEvent.Type.values()) {
            // given
            reset(videoRepository);
            when(videoRepository.findById(event.getVideoId())).thenReturn(Optional.empty());
            event.setType(type);
            // when
            processor.handleVideoEvent(event);
            // then
            verify(videoRepository).save(newVideoCaptor.capture());
            Video createdVideo = newVideoCaptor.getValue();
            assertEquals(event.getVideoId(), createdVideo.getId());
        }
    }

    @Test
    public void shouldUpdateExistingVideoOnEitherAProgressOrATimelineEvent() {
        for (KafkaVideoEvent.Type type: KafkaVideoEvent.Type.values()) {
            // given
            reset(videoRepository);
            when(videoRepository.findById(event.getVideoId())).thenReturn(Optional.of(video));
            event.setType(type);
            // when
            processor.handleVideoEvent(event);
            // then
            verify(videoRepository).save(video);
        }
    }

    @Test
    public void shouldUpdateVideosLastPlayDateOnEitherAProgressOrATimelineEvent() {
        for (KafkaVideoEvent.Type type: KafkaVideoEvent.Type.values()) {
            // given
            reset(videoRepository);
            when(videoRepository.findById(event.getVideoId())).thenReturn(Optional.of(video));
            event.setType(type);
            video.setLastPlayDate(null);
            // when
            processor.handleVideoEvent(event);
            // then
            LocalDateTime lastPlayDate = video.getLastPlayDate();
            assertEquals(LocalDateTime.of(2020, 3, 3, 19, 5, 14, 431000000), lastPlayDate);
        }
    }

    @Test
    public void shouldNotUpdateVideoRelativePathByFirstTimelineEvent() {
        // given
        event.setType(KafkaVideoEvent.Type.timeline);
        // when
        processor.handleVideoEvent(event);
        // then
        assertNull(video.getRelativePath());
    }

    @Test
    public void shouldUpdateVideoRelativePathByTimelineEventIfVideoNameIsAlreadyKnown() {
        // given
        video.setName(event.getVideoName());
        event.setType(KafkaVideoEvent.Type.timeline);
        // when
        processor.handleVideoEvent(event);
        // then
        assertEquals(service.resolvePathToVideoFile(video.getName()), video.getRelativePath());
    }

    @Test
    public void shouldUpdateVideoRelativePathByProgressEvent() {
        // given
        event.setType(KafkaVideoEvent.Type.progress);
        // when
        processor.handleVideoEvent(event);
        // then
        assertEquals(service.resolvePathToVideoFile(video.getName()), video.getRelativePath());
    }

    @Test
    public void shouldUpdateVideoRelativePathIfExistingRelativePathIsNotValidAnymore() {
        for (KafkaVideoEvent.Type type: KafkaVideoEvent.Type.values()) {
            // given
            event.setType(type);
            video.setName(event.getVideoName());
            Path oldRelativePath = Paths.get("The Office");
            Path newRelativePath = Paths.get("The Office Season 1");
            when(service.resolvePathToVideoFile(event.getVideoName())).thenReturn(oldRelativePath);
            when(service.exists(oldRelativePath.resolve(event.getVideoName()))).thenReturn(false);
            video.updateRelativePathIfNecessary(service);
            when(service.resolvePathToVideoFile(event.getVideoName())).thenReturn(newRelativePath);
            // when
            processor.handleVideoEvent(event);
            // then
            assertEquals(newRelativePath, video.getRelativePath());
        }
    }

    @Test
    public void shouldNotUpdateVideoRelativePathIfExistingRelativePathIsStillValid() {
        for (KafkaVideoEvent.Type type: KafkaVideoEvent.Type.values()) {
            // given
            event.setType(type);
            video.setName(event.getVideoName());
            Path existingPath = Paths.get("The Office");
            Path newRelativePath = Paths.get("The Office Season 2");
            when(service.resolvePathToVideoFile(event.getVideoName())).thenReturn(existingPath);
            when(service.exists(existingPath.resolve(event.getVideoName()))).thenReturn(true);
            video.updateRelativePathIfNecessary(service);
            when(service.resolvePathToVideoFile(event.getVideoName())).thenReturn(newRelativePath);
            // when
            processor.handleVideoEvent(event);
            // then
            assertEquals(existingPath, video.getRelativePath());
        }
    }

    @Test
    public void shouldUpdateVideoTimePlayedByEitherAProgressOrATimelineEvent() {
        for (KafkaVideoEvent.Type type: KafkaVideoEvent.Type.values()) {
            // given
            event.setType(type);
            video.setTimePlayed(0);
            // when
            processor.handleVideoEvent(event);
            // then
            assertEquals(event.getTimePlayed(), video.getTimePlayed());
        }
    }

    @Test
    public void shouldUpdateVideoNameByAProgressEvent() {
        // given
        event.setType(KafkaVideoEvent.Type.progress);
        // when
        processor.handleVideoEvent(event);
        // then
        assertEquals(event.getVideoName(), video.getName());
    }

    @Test
    public void shouldUpdateVideoTotalTimeByATimelineEvent() {
        // given
        event.setType(KafkaVideoEvent.Type.timeline);
        // when
        processor.handleVideoEvent(event);
        // then
        assertEquals(event.getTotalTime(), video.getTotalTime());
    }

    @Test
    public void shouldCreateNotificationAndRethrowAnErrorWhileProcessingEitherOfEvents() {
        for (KafkaVideoEvent.Type type: KafkaVideoEvent.Type.values()) {
            // given
            RuntimeException cause = new RuntimeException();
            reset(videoRepository, notificationRepository);
            when(videoRepository.findById(event.getVideoId())).thenThrow(cause);
            event.setType(type);
            try {
                // when
                processor.handleVideoEvent(event);
            } catch (Exception e) {
                // then
                verify(notificationRepository).save(notificationCaptor.capture());
                Notification notification = notificationCaptor.getValue();
                EventProcessingException expectedException = new EventProcessingException(event, cause);
                Notification expectedNotification = Notification.fromException(expectedException);
                assertEquals(expectedNotification.getContent(), notification.getContent());
                assertEquals(expectedException.getMessage(), e.getMessage());
            }
        }
    }
}
