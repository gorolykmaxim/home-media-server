package com.gorolykmaxim.videoswatched.service.eventprocessor;

import com.gorolykmaxim.videoswatched.peristence.model.Video;
import com.gorolykmaxim.videoswatched.peristence.dao.VideoDao;
import com.gorolykmaxim.videoswatched.peristence.model.WatchProgress;
import com.gorolykmaxim.videoswatched.peristence.dao.WatchProgressDao;
import com.gorolykmaxim.videoswatched.service.event.VideoEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.kafka.listener.ConsumerSeekAware;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class VideoEventsProcessorTest {
    private VideoEvent event;
    private Video video;
    private WatchProgress progress;
    private VideoDao videoDao;
    private WatchProgressDao watchProgressDao;
    private VideoEventsProcessor processor;

    @Before
    public void setUp() throws Exception {
        event = new VideoEvent();
        event.setUserId(15l);
        event.setVideoId(25l);
        event.setTimePlayed(352l);
        event.setTotalTime(231125l);
        event.setVideoName("Episode 15");
        event.setVideoGroupName("Better call Saul");
        event.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        video = new Video(event.getVideoId(), event.getVideoName(), event.getVideoGroupName(), event.getTotalTime());
        progress = new WatchProgress(event.getVideoId(), event.getUserId(), event.getTimePlayed(), event.getTimestamp());
        videoDao = Mockito.mock(VideoDao.class);
        watchProgressDao = Mockito.mock(WatchProgressDao.class);
        processor = new VideoEventsProcessor(videoDao, watchProgressDao, Mockito.mock(ConsumerSeekAware.class));
    }

    @Test
    public void shouldUpdateExistingVideoGroupName() {
        // given
        event.setType(VideoEvent.Type.metadata);
        Mockito.when(videoDao.findById(event.getVideoId()))
                .thenReturn(Optional.of(video));
        // when
        processor.handle(event);
        // then
        Mockito.verify(videoDao).updateGroupNameById(event.getVideoId(), event.getVideoGroupName());
    }

    @Test
    public void shouldCreateNewVideoBasedOfVideoGroupName() {
        // given
        event.setType(VideoEvent.Type.metadata);
        Mockito.when(videoDao.findById(event.getVideoId()))
                .thenReturn(Optional.empty());
        // when
        processor.handle(event);
        // then
        Mockito.verify(videoDao).create(event.getVideoId(), null, event.getVideoGroupName(), null);
    }

    @Test
    public void shouldUpdateExistingWatchProgressAndVideo() {
        // given
        event.setType(VideoEvent.Type.progress);
        Mockito.when(watchProgressDao.findByVideoIdAndUserId(event.getVideoId(), event.getUserId()))
                .thenReturn(Optional.of(progress));
        Mockito.when(videoDao.findById(event.getVideoId()))
                .thenReturn(Optional.of(video));
        // when
        processor.handle(event);
        // then
        Mockito.verify(watchProgressDao).updateByVideoIdAndUserId(event.getVideoId(), event.getUserId(), event.getTimePlayed(), event.getTimestamp());
        Mockito.verify(videoDao).updateNameById(event.getVideoId(), event.getVideoName());
    }

    @Test
    public void shouldCreateNewVideoAndItsWatchProgress() {
        // given
        event.setType(VideoEvent.Type.progress);
        Mockito.when(watchProgressDao.findByVideoIdAndUserId(event.getVideoId(), event.getUserId()))
                .thenReturn(Optional.empty());
        Mockito.when(videoDao.findById(event.getVideoId()))
                .thenReturn(Optional.empty());
        // when
        processor.handle(event);
        // then
        Mockito.verify(watchProgressDao).create(event.getVideoId(), event.getUserId(), event.getTimePlayed(), event.getTimestamp());
        Mockito.verify(videoDao).create(event.getVideoId(), event.getVideoName(), null, null);
    }

    @Test
    public void shouldUpdateExistingVideoTotalTime() {
        // given
        event.setType(VideoEvent.Type.timeline);
        Mockito.when(videoDao.findById(event.getVideoId()))
                .thenReturn(Optional.of(video));
        // when
        processor.handle(event);
        // then
        Mockito.verify(videoDao).updateTotalTimeById(event.getVideoId(), event.getTotalTime());
    }

    @Test
    public void shouldCreateVideoBasedOfItsTotalTime() {
        // given
        event.setType(VideoEvent.Type.timeline);
        Mockito.when(videoDao.findById(event.getVideoId()))
                .thenReturn(Optional.empty());
        // when
        processor.handle(event);
        // then
        Mockito.verify(videoDao).create(event.getVideoId(), null, null, event.getTotalTime());
    }

    @Test
    public void shouldHandleVideoLookupError() {
        // given
        event.setType(VideoEvent.Type.progress);
        Mockito.when(videoDao.findById(event.getVideoId()))
                .thenThrow(new RuntimeException());
        // when
        processor.handle(event);
    }

    @Test
    public void shouldHandleWatchProgressLookupError() {
        // given
        event.setType(VideoEvent.Type.progress);
        Mockito.when(watchProgressDao.findByVideoIdAndUserId(event.getVideoId(), event.getUserId()))
                .thenThrow(new RuntimeException());
        // when
        processor.handle(event);
    }
}
