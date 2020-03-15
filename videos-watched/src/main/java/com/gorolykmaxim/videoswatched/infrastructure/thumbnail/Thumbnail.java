package com.gorolykmaxim.videoswatched.infrastructure.thumbnail;

public class Thumbnail {
    private String thumbnailName;
    private String relativeVideoFilePath;

    public Thumbnail(String thumbnailName, String relativeVideoFilePath) {
        this.thumbnailName = thumbnailName;
        this.relativeVideoFilePath = relativeVideoFilePath;
    }

    public String getThumbnailName() {
        return thumbnailName;
    }

    public String getRelativeVideoFilePath() {
        return relativeVideoFilePath;
    }
}
