package com.gorolykmaxim.videoswatched.domain.video;

import org.springframework.http.ResponseEntity;

import java.nio.file.Path;

public interface VideoThumbnailService {
    void createThumbnailForVideo(Path relativeVideoPath, String thumbnailName);
    ResponseEntity<?> downloadThumbnail(String thumbnailName);
}
