package com.gorolykmaxim.thumbnail.domain;

import java.nio.file.Path;
import java.util.List;

public interface ThumbnailService {
    List<Path> findAllThumbnailsInFolder(Path thumbnailFolder);
    boolean exists(Path absoluteThumbnailFilePath);
    void removeThumbnail(Path thumbnailPath);
}
