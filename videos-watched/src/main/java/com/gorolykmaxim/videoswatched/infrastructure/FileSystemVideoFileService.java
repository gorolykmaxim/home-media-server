package com.gorolykmaxim.videoswatched.infrastructure;

import com.gorolykmaxim.videoswatched.domain.notification.Notification;
import com.gorolykmaxim.videoswatched.domain.notification.NotificationRepository;
import com.gorolykmaxim.videoswatched.domain.video.VideoFileService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

public class FileSystemVideoFileService implements VideoFileService {
    private Path libraryRoot;
    private NotificationRepository repository;

    public FileSystemVideoFileService(Path libraryRoot, NotificationRepository repository) {
        this.libraryRoot = libraryRoot;
        this.repository = repository;
    }

    @Override
    public boolean exists(Path videoFilePath) {
        return Files.exists(libraryRoot.resolve(videoFilePath));
    }

    @Override
    public Path resolvePathToVideoFile(String videoFileName) {
        try {
            final String normalizedFileName = StringUtils.strip(videoFileName, "'");
            Collection<File> matchingFiles = FileUtils.listFiles(libraryRoot.toFile(), new PrefixFileFilter(normalizedFileName), TrueFileFilter.INSTANCE);
            if (matchingFiles.size() > 1) {
                Notification notification = Notification.createNew(String.format("While trying to find '%s' inside '%s' more than one file was found: '%s'. Rename files so that each video file name is unique.", normalizedFileName, libraryRoot, matchingFiles.toString()));
                repository.save(notification);
            }
            return matchingFiles
                    .stream()
                    .map(f -> libraryRoot.relativize(f.toPath()))
                    .findFirst()
                    .orElseThrow(() -> new VideoFileDoesNotExistException(libraryRoot, normalizedFileName));
        } catch (Exception e) {
            throw new VideoFileLookupException(videoFileName, libraryRoot, e);
        }
    }
}
