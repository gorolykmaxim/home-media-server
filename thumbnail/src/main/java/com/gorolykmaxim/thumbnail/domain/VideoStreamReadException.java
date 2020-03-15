package com.gorolykmaxim.thumbnail.domain;

import java.nio.file.Path;

public class VideoStreamReadException extends RuntimeException {
    public VideoStreamReadException(Path videoFilePath, Throwable cause) {
        super(String.format("Failed to read video streams information about %s. Reason: %s", videoFilePath, cause.getMessage()), cause);
    }
}
