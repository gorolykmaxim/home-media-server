package com.gorolykmaxim.thumbnail.domain;

public class ThumbnailRemovalException extends RuntimeException {
    public ThumbnailRemovalException(String thumbnailFileName, Throwable cause) {
        super(String.format("Failed to remove thumbnail %s. Reason: %s", thumbnailFileName, cause.getMessage()), cause);
    }
}
