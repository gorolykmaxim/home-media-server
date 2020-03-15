package com.gorolykmaxim.thumbnail.domain;

import java.nio.file.Path;

public class NoVideoStreamsFoundException extends RuntimeException {
    public NoVideoStreamsFoundException(Path absoluteVideoFilePath) {
        super(String.format("No video streams where found in '%s'", absoluteVideoFilePath));
    }
}
