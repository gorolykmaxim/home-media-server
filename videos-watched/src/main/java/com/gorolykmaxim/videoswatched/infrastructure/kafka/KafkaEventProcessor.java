package com.gorolykmaxim.videoswatched.infrastructure.kafka;

import com.gorolykmaxim.videoswatched.domain.notification.Notification;
import com.gorolykmaxim.videoswatched.domain.notification.NotificationRepository;
import com.gorolykmaxim.videoswatched.domain.video.VideoFileService;
import com.gorolykmaxim.videoswatched.domain.video.VideoPlayedEvent;
import com.gorolykmaxim.videoswatched.domain.video.VideoProgressChangedEvent;
import com.gorolykmaxim.videoswatched.domain.video.VideoRepository;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;

import javax.transaction.Transactional;

public class KafkaEventProcessor {
    private VideoRepository videoRepository;
    private NotificationRepository notificationRepository;
    private VideoFileService service;
    private Logger logger;

    public KafkaEventProcessor(VideoRepository videoRepository, NotificationRepository notificationRepository,
                               VideoFileService service, Logger logger) {
        this.videoRepository = videoRepository;
        this.notificationRepository = notificationRepository;
        this.service = service;
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
                            service
                    ).run();
                    break;
                case timeline:
                    new VideoProgressChangedEvent(
                            event.getVideoId(),
                            event.getTimestamp(),
                            event.getTimePlayed(),
                            event.getTotalTime(),
                            videoRepository,
                            service
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
