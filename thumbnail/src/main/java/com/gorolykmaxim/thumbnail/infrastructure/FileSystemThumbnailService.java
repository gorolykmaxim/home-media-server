package com.gorolykmaxim.thumbnail.infrastructure;

import com.gorolykmaxim.thumbnail.domain.ThumbnailLookupException;
import com.gorolykmaxim.thumbnail.domain.ThumbnailRemovalException;
import com.gorolykmaxim.thumbnail.domain.ThumbnailService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FileSystemThumbnailService implements ThumbnailService {
    @Override
    public List<Path> findAllThumbnailsInFolder(Path thumbnailFolder) {
        try {
            return Files.walk(thumbnailFolder).filter(path -> !Files.isDirectory(path)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new ThumbnailLookupException(thumbnailFolder, e);
        }
    }

    @Override
    public boolean exists(Path absoluteThumbnailFilePath) {
        return Files.exists(absoluteThumbnailFilePath);
    }

    @Override
    public void removeThumbnail(Path thumbnailPath) {
        try {
            Files.deleteIfExists(thumbnailPath);
        } catch (IOException e) {
            throw new ThumbnailRemovalException(thumbnailPath.toString(), e);
        }
    }
}
