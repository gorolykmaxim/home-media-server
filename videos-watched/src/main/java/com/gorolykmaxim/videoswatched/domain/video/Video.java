package com.gorolykmaxim.videoswatched.domain.video;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "VIDEO")
public class Video {
    @Id
    @Column(name = "VIDEO_ID")
    private long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "TIME_PLAYED")
    private Long timePlayed;
    @Column(name = "TOTAL_TIME")
    private Long totalTime;
    @Column(name = "RELATIVE_PATH")
    private String relativePath;
    @Column(name = "LAST_PLAY_DATE")
    private LocalDateTime lastPlayDate;

    Video() {}

    public Video(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimePlayed(long timePlayed) {
        this.timePlayed = timePlayed;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public void setLastPlayDate(LocalDateTime lastPlayDate) {
        this.lastPlayDate = lastPlayDate;
    }

    public void updateRelativePathIfNecessary(VideoFileService service) {
        if (name == null) {
            // We don't know the name of the file yet, so there is no reason to try to find it.
            return;
        }
        Path relativePath = getRelativePath();
        if (relativePath == null || !service.exists(relativePath.resolve(name))) {
            this.relativePath = service.resolvePathToVideoFile(name).toString();
        }
    }

    public void createThumbnailIfPossible(VideoThumbnailService service) {
        if (relativePath == null) {
            // We don't know the relative path to the video file yet, so we won't be able to generate a thumbnail for it.
            return;
        }
        service.createThumbnailForVideo(getRelativePath(), Long.toString(id));
    }

    public boolean hasAllInformation() {
        return name != null && timePlayed != null && totalTime != null;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getTimePlayed() {
        return timePlayed;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public LocalDateTime getLastPlayDate() {
        return lastPlayDate;
    }

    public Path getRelativePath() {
        return relativePath != null ? Paths.get(relativePath) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(id, video.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
