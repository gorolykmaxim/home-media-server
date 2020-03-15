package com.gorolykmaxim.videoswatched.domain.video;

import java.time.LocalDateTime;

public class VideoProgressChangedEvent extends VideoEvent {
    private final long timePlayed;
    private final long totalTime;

    public VideoProgressChangedEvent(long videoId, LocalDateTime dateTime, long timePlayed, long totalTime,
                                     VideoRepository repository, VideoFileService fileService,
                                     VideoThumbnailService thumbnailService) {
        super(videoId, dateTime, repository, fileService, thumbnailService);
        this.timePlayed = timePlayed;
        this.totalTime = totalTime;
    }

    @Override
    protected void process(Video video) {
        video.setTimePlayed(timePlayed);
        video.setTotalTime(totalTime);
    }
}
