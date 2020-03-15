package com.gorolykmaxim.videoswatched.domain.video;

import java.nio.file.Path;

public interface VideoThumbnailService {
    void createThumbnailForVideo(Path relativeVideoPath, String thumbnailName);
}
