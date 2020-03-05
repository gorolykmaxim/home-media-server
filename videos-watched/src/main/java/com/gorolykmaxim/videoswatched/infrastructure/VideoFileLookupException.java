package com.gorolykmaxim.videoswatched.infrastructure;

import java.nio.file.Path;

public class VideoFileLookupException extends RuntimeException {
    public VideoFileLookupException(String videoFileName, Path libraryRoot, Throwable cause) {
        super(String.format("Failed to find video file with name '%s' in the library root '%s'. Reason: %s", videoFileName, libraryRoot, cause.getMessage()), cause);
    }
}
