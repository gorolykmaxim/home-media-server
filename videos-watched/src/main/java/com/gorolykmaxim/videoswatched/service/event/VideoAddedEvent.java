package com.gorolykmaxim.videoswatched.service.event;

public interface VideoAddedEvent {
    long getVideoId();
    String getVideoGroupName();
}
