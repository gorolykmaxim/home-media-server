package com.gorolykmaxim.videoswatched.peristence.sql;

import com.gorolykmaxim.videoswatched.peristence.model.Video;
import com.gorolykmaxim.videoswatched.peristence.model.VideoWatchProgress;
import com.gorolykmaxim.videoswatched.peristence.model.WatchProgress;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class VideoWatchProgressRowMapper implements RowMapper<VideoWatchProgress> {
    @Override
    public VideoWatchProgress mapRow(ResultSet rs, int rowNum) throws SQLException {
        String name = rs.getString("name");
        String groupName = rs.getString("group_name");
        Long totalTime = rs.getLong("total_time");
        totalTime = rs.wasNull() ? null  : totalTime;
        long videoId = rs.getLong("video_id");
        Video video = new Video(videoId, name, groupName, totalTime);
        long userId = rs.getLong("user_id");
        long timePlayed = rs.getLong("time_played");
        LocalDateTime lastWatchDate = rs.getTimestamp("last_watch_date").toLocalDateTime();
        WatchProgress progress = new WatchProgress(videoId, userId, timePlayed, lastWatchDate);
        return new VideoWatchProgress(video, progress);
    }
}
