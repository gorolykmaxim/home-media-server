package com.gorolykmaxim.videoswatched.readmodel;

import java.util.Objects;
import java.util.TreeSet;

public class VideoGroupReadModel implements Comparable<VideoGroupReadModel> {
    private final String name;
    private final TreeSet<VideoReadModel> videos;

    public VideoGroupReadModel(String name) {
        this.name = name;
        videos = new TreeSet<>();
    }

    public int getId() {
        return Objects.hash(name);
    }

    public void addVideo(VideoReadModel video) {
        videos.add(video);
    }

    public String getName() {
        return name;
    }

    public Iterable<VideoReadModel> getVideos() {
        return videos;
    }

    public VideoReadModel getLatestWatchedVideo() {
        return videos.first();
    }

    @Override
    public int compareTo(VideoGroupReadModel o) {
        VideoReadModel myLastVideo = getLatestWatchedVideo();
        VideoReadModel otherLastVideo = o.getLatestWatchedVideo();
        return myLastVideo.compareTo(otherLastVideo);
    }
}
