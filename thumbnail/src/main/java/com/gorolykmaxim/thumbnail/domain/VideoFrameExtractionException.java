package com.gorolykmaxim.thumbnail.domain;

import java.nio.file.Path;
import java.time.Duration;

public class VideoFrameExtractionException extends RuntimeException {
    public VideoFrameExtractionException(Path videoFilePath, Path outputImageFilePath, Duration startOffset, Throwable cause) {
        super(String.format("Failed to save frame of the video %s, starting at %s into the %s. Reason: %s", videoFilePath, startOffset, outputImageFilePath, cause.getMessage()), cause);
    }
}
