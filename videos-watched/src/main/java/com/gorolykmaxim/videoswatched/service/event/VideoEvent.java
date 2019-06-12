package com.gorolykmaxim.videoswatched.service.event;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class VideoEvent implements VideoAddedEvent, VideoPlayedByUserEvent, VideoTimelineChangedEvent {
    private Type type;
    private Long timePlayed;
    private Long totalTime;
    private Long videoId;
    private String videoName;
    private Long userId;
    private String videoGroupName;
    private LocalDateTime timestamp;

    public void setType(Type type) {
        this.type = type;
    }

    public void setTimePlayed(Long timePlayed) {
        this.timePlayed = timePlayed;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setVideoGroupName(String videoGroupName) {
        this.videoGroupName = videoGroupName;
    }

    @JsonSetter("@timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME);
    }

    public Type getType() {
        return type;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public long getVideoId() {
        return videoId;
    }

    @Override
    public String getVideoName() {
        return videoName;
    }

    @Override
    public long getTimePlayed() {
        return timePlayed;
    }

    @Override
    public long getTotalTime() {
        return totalTime;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String getVideoGroupName() {
        return videoGroupName;
    }

    public enum Type {
        timeline, progress, metadata
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoEvent that = (VideoEvent) o;
        return type == that.type &&
                Objects.equals(timePlayed, that.timePlayed) &&
                Objects.equals(totalTime, that.totalTime) &&
                Objects.equals(videoId, that.videoId) &&
                Objects.equals(videoName, that.videoName) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(videoGroupName, that.videoGroupName) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, timePlayed, totalTime, videoId, videoName, userId, videoGroupName, timestamp);
    }

    @Override
    public String toString() {
        return "VideoEvent{" +
                "type=" + type +
                ", timePlayed=" + timePlayed +
                ", totalTime=" + totalTime +
                ", videoId=" + videoId +
                ", videoName='" + videoName + '\'' +
                ", userId=" + userId +
                ", videoGroupName='" + videoGroupName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
