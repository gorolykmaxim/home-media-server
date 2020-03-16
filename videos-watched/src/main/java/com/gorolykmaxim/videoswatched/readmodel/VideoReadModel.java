package com.gorolykmaxim.videoswatched.readmodel;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Objects;

public class VideoReadModel implements Comparable<VideoReadModel> {
    private final long id;
    private final String name;
    private final Long timePlayed;
    private final Long totalTime;
    private final LocalDateTime lastPlayDate;
    private final String durationFormat;
    private final Clock clock;

    public VideoReadModel(long id, String name, Long timePlayed, Long totalTime, LocalDateTime lastPlayDate, String durationFormat, Clock clock) {
        this.id = id;
        this.name = name;
        this.timePlayed = timePlayed;
        this.totalTime = totalTime;
        this.lastPlayDate = lastPlayDate;
        this.durationFormat = durationFormat;
        this.clock = clock;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTimePlayed() {
        return DurationFormatUtils.formatDuration(timePlayed, durationFormat, false);
    }

    public String getTotalTime() {
        return DurationFormatUtils.formatDuration(totalTime, durationFormat, false);
    }

    public String getLastPlayedDate() {
        LocalDate now = LocalDate.now(clock);
        Period timeAgo = Period.between(lastPlayDate.toLocalDate(), now);
        int days = timeAgo.getDays();
        return days > 0 ? String.format("%d days ago", days) : "Today";
    }

    @Override
    public int compareTo(VideoReadModel o) {
        // Video with the latest play date go first.
        int difference = lastPlayDate.compareTo(o.lastPlayDate) * -1;
        return difference == 0 ? name.compareTo(o.name) : difference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoReadModel that = (VideoReadModel) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
