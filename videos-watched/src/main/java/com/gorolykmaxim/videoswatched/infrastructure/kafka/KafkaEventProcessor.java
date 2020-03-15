package com.gorolykmaxim.videoswatched.infrastructure.kafka;

import com.gorolykmaxim.videoswatched.domain.notification.Notification;
import com.gorolykmaxim.videoswatched.domain.notification.NotificationRepository;
import com.gorolykmaxim.videoswatched.domain.video.*;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;

import javax.transaction.Transactional;

public class KafkaEventProcessor {
    private VideoRepository videoRepository;
    private NotificationRepository notificationRepository;
    private VideoFileService videoFileService;
    private VideoThumbnailService thumbnailService;
    private Logger logger;

    public KafkaEventProcessor(VideoRepository videoRepository, NotificationRepository notificationRepository,
                               VideoFileService fileService, VideoThumbnailService thumbnailService,
                               Logger logger) {
        this.videoRepository = videoRepository;
        this.notificationRepository = notificationRepository;
        this.videoFileService = fileService;
        this.thumbnailService = thumbnailService;
        this.logger = logger;
    }

    @Transactional
    @KafkaListener(topics = "video")
    public void handleVideoEvent(KafkaVideoEvent event) {
        try {
            logger.info("Processing event: {}", event);
            switch (event.getType()) {
                case progress:
                    new VideoPlayedEvent(
                            event.getVideoId(),
                            event.getTimestamp(),
                            event.getVideoName(),
                            event.getTimePlayed(),
                            videoRepository,
                            videoFileService,
                            thumbnailService
                    ).run();
                    break;
                case timeline:
                    new VideoProgressChangedEvent(
                            event.getVideoId(),
                            event.getTimestamp(),
                            event.getTimePlayed(),
                            event.getTotalTime(),
                            videoRepository,
                            videoFileService,
                            thumbnailService
                    ).run();
                    break;
            }
        } catch (Exception e) {
            EventProcessingException exception = new EventProcessingException(event, e);
            Notification notification = Notification.fromException(exception);
            notificationRepository.save(notification);
            throw exception;
        }
    }
}
