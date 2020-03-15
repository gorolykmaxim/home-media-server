package com.gorolykmaxim.thumbnail.domain;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;

public class MediaFile {
    private Path rootFolder;
    private Path relativeFilePath;
    private String extension;

    public MediaFile(Path rootFolder, Path relativeFilePath) {
        this.rootFolder = rootFolder;
        this.relativeFilePath = relativeFilePath;
    }

    public MediaFile(Path rootFolder, Path relativeFilePath, String extension) {
        this.rootFolder = rootFolder;
        this.relativeFilePath = relativeFilePath;
        this.extension = extension;
    }

    public Path getAbsolutePath() {
        if (extension == null) {
            return rootFolder.resolve(relativeFilePath);
        } else {
            String relativeFilePathWithoutExtension = FilenameUtils.removeExtension(relativeFilePath.toString());
            return rootFolder.resolve(relativeFilePathWithoutExtension + "." + extension);
        }
    }

    public String getName() {
        return FilenameUtils.removeExtension(relativeFilePath.getFileName().toString());
    }
}
