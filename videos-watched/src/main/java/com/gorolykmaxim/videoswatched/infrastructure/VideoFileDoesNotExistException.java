package com.gorolykmaxim.videoswatched.infrastructure;

import java.nio.file.Path;

public class VideoFileDoesNotExistException extends RuntimeException {
    public VideoFileDoesNotExistException(Path directory, String videoFileName) {
        super(String.format("Video file with name '%s' does not exist inside the directory '%s'", videoFileName, directory));
    }
}
