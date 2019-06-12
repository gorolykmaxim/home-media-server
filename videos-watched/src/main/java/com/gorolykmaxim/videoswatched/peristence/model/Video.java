package com.gorolykmaxim.videoswatched.peristence.model;

import java.util.Objects;
import java.util.Optional;

public class Video {
    private final long id;
    private final String name;
    private final String groupName;
    private final Long totalTime;

    public Video(long id, String name, String groupName, Long totalTime) {
        this.id = id;
        this.name = name;
        this.groupName = groupName;
        this.totalTime = totalTime;
    }

    public long getId() {
        return id;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<String> getGroupName() {
        return Optional.ofNullable(groupName);
    }

    public Optional<Long> getTotalTime() {
        return Optional.ofNullable(totalTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return id == video.id &&
                Objects.equals(name, video.name) &&
                Objects.equals(groupName, video.groupName) &&
                Objects.equals(totalTime, video.totalTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, groupName, totalTime);
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", groupName='" + groupName + '\'' +
                ", totalTime=" + totalTime +
                '}';
    }
}
