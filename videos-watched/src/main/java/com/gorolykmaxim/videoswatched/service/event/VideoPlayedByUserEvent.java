package com.gorolykmaxim.videoswatched.service.event;

import java.time.LocalDateTime;

public interface VideoPlayedByUserEvent {
    long getUserId();
    long getVideoId();
    String getVideoName();
    long getTimePlayed();
    LocalDateTime getTimestamp();
}
