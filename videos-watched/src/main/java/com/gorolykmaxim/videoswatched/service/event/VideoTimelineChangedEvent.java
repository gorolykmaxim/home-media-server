package com.gorolykmaxim.videoswatched.service.event;

import java.time.LocalDateTime;

public interface VideoTimelineChangedEvent {
    long getVideoId();
    long getTimePlayed();
    long getTotalTime();
    LocalDateTime getTimestamp();
}
