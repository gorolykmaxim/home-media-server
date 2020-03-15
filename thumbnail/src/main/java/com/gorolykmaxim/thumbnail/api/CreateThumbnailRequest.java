package com.gorolykmaxim.thumbnail.api;

import javax.validation.constraints.NotNull;

public class CreateThumbnailRequest {
    @NotNull
    private String thumbnailName;
    @NotNull
    private String relativeVideoFilePath;

    public String getThumbnailName() {
        return thumbnailName;
    }

    public void setThumbnailName(String thumbnailName) {
        this.thumbnailName = thumbnailName;
    }

    public String getRelativeVideoFilePath() {
        return relativeVideoFilePath;
    }

    public void setRelativeVideoFilePath(String relativeVideoFilePath) {
        this.relativeVideoFilePath = relativeVideoFilePath;
    }

    @Override
    public String toString() {
        return "CreateThumbnailRequest{" +
                "thumbnailName='" + thumbnailName + '\'' +
                ", relativeVideoFilePath='" + relativeVideoFilePath + '\'' +
                '}';
    }
}
