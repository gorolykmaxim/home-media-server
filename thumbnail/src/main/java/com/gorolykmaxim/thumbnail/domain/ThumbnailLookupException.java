package com.gorolykmaxim.thumbnail.domain;

import java.nio.file.Path;

public class ThumbnailLookupException extends RuntimeException {
    public ThumbnailLookupException(Path folder, Throwable cause) {
        super(String.format("Failed to find thumbnails in folder %s. Reason: %s", folder, cause.getMessage()), cause);
    }
}
