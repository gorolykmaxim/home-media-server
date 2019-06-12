package com.gorolykmaxim.videoswatched.peristence.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class VideoWatchProgress {
    private final Video video;
    private final WatchProgress watchProgress;

    public VideoWatchProgress(Video video, WatchProgress watchProgress) {
        this.video = video;
        this.watchProgress = watchProgress;
    }

    public long getId() {
        return video.getId();
    }

    public Optional<Long> getProgress() {
        long timePlayed = watchProgress.getTimePlayed();
        Optional<Long> possibleTotalTime = video.getTotalTime();
        if (possibleTotalTime.isPresent()) {
            long totalTime = possibleTotalTime.get();
            return Optional.of((long)((double)timePlayed / (double) totalTime * 100));
        } else {
            return Optional.empty();
        }
    }

    public boolean isMoreRecentThen(VideoWatchProgress progress) {
        return watchProgress.getLastWatchDate().isAfter(progress.getLastWatchDate());
    }

    public Optional<String> getName() {
        return video.getName();
    }

    public Optional<String> getGroupName() {
        return video.getGroupName();
    }

    public long getUserId() {
        return watchProgress.getUserId();
    }

    public long getTimePlayed() {
        return watchProgress.getTimePlayed();
    }

    public Optional<Long> getTotalTime() {
        return video.getTotalTime();
    }

    public LocalDateTime getLastWatchDate() {
        return watchProgress.getLastWatchDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoWatchProgress that = (VideoWatchProgress) o;
        return Objects.equals(video, that.video) &&
                Objects.equals(watchProgress, that.watchProgress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(video, watchProgress);
    }

    @Override
    public String toString() {
        return "VideoWatchProgress{" +
                "video=" + video +
                ", watchProgress=" + watchProgress +
                '}';
    }
}
