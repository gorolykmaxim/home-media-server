package com.gorolykmaxim.videoswatched.domain.video;

import java.time.LocalDateTime;

public class VideoPlayedEvent extends VideoEvent {
    private final String videoName;
    private final long timePlayed;

    public VideoPlayedEvent(long videoId, LocalDateTime dateTime, String videoName, long timePlayed,
                            VideoRepository repository, VideoFileService service) {
        super(videoId, dateTime, repository, service);
        this.videoName = videoName;
        this.timePlayed = timePlayed;
    }

    @Override
    protected void process(Video video) {
        video.setName(videoName);
        video.setTimePlayed(timePlayed);
    }
}
