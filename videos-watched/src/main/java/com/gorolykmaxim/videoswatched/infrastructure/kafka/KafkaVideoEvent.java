package com.gorolykmaxim.videoswatched.infrastructure.kafka;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class KafkaVideoEvent {
    private Type type;
    private Long timePlayed;
    private Long totalTime;
    private Long videoId;
    private String videoName;
    private LocalDateTime timestamp;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Long getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(Long timePlayed) {
        this.timePlayed = timePlayed;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @JsonSetter("@timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME);
    }

    @Override
    public String toString() {
        return "KafkaVideoEvent{" +
                "type='" + type + '\'' +
                ", timePlayed=" + timePlayed +
                ", totalTime=" + totalTime +
                ", videoId=" + videoId +
                ", videoName='" + videoName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public enum Type {
        timeline, progress
    }
}
