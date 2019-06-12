package com.gorolykmaxim.videoswatched.peristence.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class WatchProgress {
    private final long videoId;
    private final Long userId;
    private final long timePlayed;
    private final LocalDateTime lastWatchDate;

    public WatchProgress(long videoId, long userId, long timePlayed, LocalDateTime lastWatchDate) {
        this.videoId = videoId;
        this.userId = userId;
        this.timePlayed = timePlayed;
        this.lastWatchDate = lastWatchDate;
    }

    public long getVideoId() {
        return videoId;
    }

    public long getUserId() {
        return userId;
    }

    public long getTimePlayed() {
        return timePlayed;
    }

    public LocalDateTime getLastWatchDate() {
        return lastWatchDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WatchProgress that = (WatchProgress) o;
        return videoId == that.videoId &&
                timePlayed == that.timePlayed &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(lastWatchDate, that.lastWatchDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, userId, timePlayed, lastWatchDate);
    }

    @Override
    public String toString() {
        return "WatchProgress{" +
                "videoId=" + videoId +
                ", userId=" + userId +
                ", timePlayed=" + timePlayed +
                ", lastWatchDate=" + lastWatchDate +
                '}';
    }
}
