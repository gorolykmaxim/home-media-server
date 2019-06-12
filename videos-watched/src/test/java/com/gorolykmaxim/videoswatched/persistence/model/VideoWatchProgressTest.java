package com.gorolykmaxim.videoswatched.persistence.model;

import com.gorolykmaxim.videoswatched.peristence.model.Video;
import com.gorolykmaxim.videoswatched.peristence.model.VideoWatchProgress;
import com.gorolykmaxim.videoswatched.peristence.model.WatchProgress;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Optional;

public class VideoWatchProgressTest {

    @Test
    public void shouldReturnProgress() {
        // given
        Video video = new Video(15l, "Episode 3", "Star Wars", 3294725235l);
        WatchProgress watchProgress = new WatchProgress(video.getId(), 2l, 4352l, LocalDateTime.now());
        Long expectedProgress = (long)((double)watchProgress.getTimePlayed() / (double)video.getTotalTime().get() * 100);
        VideoWatchProgress videoWatchProgress = new VideoWatchProgress(video, watchProgress);
        // when
        Optional<Long> possibleProgress = videoWatchProgress.getProgress();
        // then
        Assert.assertTrue(possibleProgress.isPresent());
        Assert.assertEquals(expectedProgress, possibleProgress.get());
    }

    @Test
    public void shouldNotTryToCalculateProgressIfTotalTimeIsUnknown() {
        // given
        Video video = new Video(15l, "Episode 3", "Star Wars", null);
        WatchProgress watchProgress = new WatchProgress(video.getId(), 2l, 4352l, LocalDateTime.now());
        VideoWatchProgress videoWatchProgress = new VideoWatchProgress(video, watchProgress);
        // when
        Optional<Long> possibleProgress = videoWatchProgress.getProgress();
        // then
        Assert.assertFalse(possibleProgress.isPresent());
    }

    @Test
    public void shouldBeMoreRecent() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Video video = new Video(15l, "Episode 3", "Star Wars", 3294725235l);
        WatchProgress watchProgress = new WatchProgress(video.getId(), 2l, 4352l, now);
        VideoWatchProgress recentVideoWatchProgress = new VideoWatchProgress(video, watchProgress);
        watchProgress = new WatchProgress(video.getId(), 2l, 4352l, now.minusDays(2));
        VideoWatchProgress videoWatchProgress = new VideoWatchProgress(video, watchProgress);
        // when
        boolean isMoreRecent = recentVideoWatchProgress.isMoreRecentThen(videoWatchProgress);
        boolean isNotMoreRecent = videoWatchProgress.isMoreRecentThen(recentVideoWatchProgress);
        // then
        Assert.assertTrue(isMoreRecent);
        Assert.assertFalse(isNotMoreRecent);
    }
}
