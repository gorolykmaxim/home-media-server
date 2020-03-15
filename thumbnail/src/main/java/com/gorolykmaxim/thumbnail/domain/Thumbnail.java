package com.gorolykmaxim.thumbnail.domain;

import java.time.Duration;
import java.util.List;

public class Thumbnail {
    private MediaFile file;

    public Thumbnail(MediaFile file) {
        this.file = file;
    }

    public void generateIfNotExistFor(MediaFile videoFile, VideoService videoService, ThumbnailService thumbnailService) {
        if (thumbnailService.exists(file.getAbsolutePath())) {
            return;
        }
        List<VideoStream> videoStreams = videoService.getStreamsOfFile(videoFile.getAbsolutePath());
        if (videoStreams.isEmpty()) {
            throw new NoVideoStreamsFoundException(videoFile.getAbsolutePath());
        }
        VideoStream videoStream = videoStreams.get(0);
        Duration videoDuration = videoStream.getDuration();
        Duration thumbnailTimestamp = videoDuration.dividedBy(2);
        videoService.extractFrameAsImageFile(videoFile.getAbsolutePath(), file.getAbsolutePath(), thumbnailTimestamp);
    }
}
