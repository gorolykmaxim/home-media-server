package com.gorolykmaxim.videoswatched.domain.video;

import java.time.LocalDateTime;

public abstract class VideoEvent implements Runnable {
    private final long videoId;
    private final LocalDateTime dateTime;
    private VideoRepository repository;
    private VideoFileService service;

    public VideoEvent(long videoId, LocalDateTime dateTime, VideoRepository repository, VideoFileService service) {
        this.videoId = videoId;
        this.dateTime = dateTime;
        this.repository = repository;
        this.service = service;
    }

    @Override
    public void run() {
        Video video = repository.findById(videoId).orElse(new Video(videoId));
        process(video);
        video.setLastPlayDate(dateTime);
        video.updateRelativePathIfNecessary(service);
        repository.save(video);
    }

    protected abstract void process(Video video);
}
