package com.gorolykmaxim.videoswatched.service.eventprocessor;

import com.gorolykmaxim.videoswatched.peristence.model.Video;
import com.gorolykmaxim.videoswatched.peristence.dao.VideoDao;
import com.gorolykmaxim.videoswatched.peristence.model.WatchProgress;
import com.gorolykmaxim.videoswatched.peristence.dao.WatchProgressDao;
import com.gorolykmaxim.videoswatched.service.event.VideoAddedEvent;
import com.gorolykmaxim.videoswatched.service.event.VideoEvent;
import com.gorolykmaxim.videoswatched.service.event.VideoPlayedByUserEvent;
import com.gorolykmaxim.videoswatched.service.event.VideoTimelineChangedEvent;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

public class VideoEventsProcessor implements ConsumerSeekAware {
    private Logger log;
    private VideoDao videoDao;
    private WatchProgressDao watchProgressDao;
    private ConsumerSeekAware offsetResolutionStrategy;

    public VideoEventsProcessor(VideoDao videoDao, WatchProgressDao watchProgressDao, ConsumerSeekAware offsetResolutionStrategy) {
        this.watchProgressDao = watchProgressDao;
        this.offsetResolutionStrategy = offsetResolutionStrategy;
        this.videoDao = videoDao;
        log = LoggerFactory.getLogger(VideoEventsProcessor.class);
    }

    @KafkaListener(topics = "video", containerFactory = "videoEventConcurrentKafkaListenerContainerFactory")
    @Transactional
    public void handle(VideoEvent event) {
        try {
            log.debug("Incoming event: {}", event);
            switch (event.getType()) {
                case metadata:
                    handle((VideoAddedEvent) event);
                    break;
                case progress:
                    handle((VideoPlayedByUserEvent) event);
                    break;
                case timeline:
                    handle((VideoTimelineChangedEvent) event);
                    break;
            }
        } catch (RuntimeException e) {
            log.error("Failed to process event: {}. Reason: {}", event, e.getMessage());
        }
    }

    private void handle(VideoAddedEvent event) {
        Optional<Video> possibleProgress = videoDao.findById(event.getVideoId());
        if (possibleProgress.isPresent()) {
            log.debug("Found existing video: {}. Going to update it.", possibleProgress.get());
            videoDao.updateGroupNameById(event.getVideoId(), event.getVideoGroupName());
        } else {
            log.debug("Going to create a new video based of event {}.", event);
            videoDao.create(event.getVideoId(), null, event.getVideoGroupName(), null);
        }
    }

    private void handle(VideoPlayedByUserEvent event) {
        Optional<Video> possibleVideo = videoDao.findById(event.getVideoId());
        Optional<WatchProgress> possibleProgress = watchProgressDao.findByVideoIdAndUserId(event.getVideoId(), event.getUserId());
        if (possibleProgress.isPresent()) {
            log.debug("Found existing watch progress: {}. Going to update it.", possibleProgress.get());
            watchProgressDao.updateByVideoIdAndUserId(event.getVideoId(), event.getUserId(), event.getTimePlayed(), event.getTimestamp());
        } else {
            log.debug("Going to create a new watch progress based of event {}.", event);
            watchProgressDao.create(event.getVideoId(), event.getUserId(), event.getTimePlayed(), event.getTimestamp());
        }
        if (possibleVideo.isPresent()) {
            log.debug("Found existing video: {}. Going to update it.", possibleVideo.get());
            videoDao.updateNameById(event.getVideoId(), event.getVideoName());
        } else {
            log.debug("Going to create a new video based of event {}.", event);
            videoDao.create(event.getVideoId(), event.getVideoName(), null, null);
        }
    }

    private void handle(VideoTimelineChangedEvent event) {
        Optional<Video> possibleVideo = videoDao.findById(event.getVideoId());
        if (possibleVideo.isPresent()) {
            log.debug("Found existing video: {}. Going to update it.", possibleVideo.get());
            videoDao.updateTotalTimeById(event.getVideoId(), event.getTotalTime());
        } else {
            log.debug("Going to create a new video based of event {}.", event);
            videoDao.create(event.getVideoId(), null, null, event.getTotalTime());
        }
    }

    @Override
    public void registerSeekCallback(ConsumerSeekCallback callback) {
        offsetResolutionStrategy.registerSeekCallback(callback);
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        offsetResolutionStrategy.onPartitionsAssigned(assignments, callback);
    }

    @Override
    public void onIdleContainer(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        offsetResolutionStrategy.onIdleContainer(assignments, callback);
    }
}
