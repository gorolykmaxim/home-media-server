package com.gorolykmaxim.videoswatched.infrastructure.thumbnail;

import java.nio.file.Path;

public class ThumbnailCreationException extends RuntimeException {
    public ThumbnailCreationException(String thumbnailName, Path relativeVideoPath, Throwable cause) {
        super(String.format("Failed to create a thumbnail %s for a video %s. Reason: %s", thumbnailName, relativeVideoPath, cause.getMessage()), cause);
    }
}
