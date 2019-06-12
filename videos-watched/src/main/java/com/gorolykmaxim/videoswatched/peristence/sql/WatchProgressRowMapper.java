package com.gorolykmaxim.videoswatched.peristence.sql;

import com.gorolykmaxim.videoswatched.peristence.model.WatchProgress;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WatchProgressRowMapper implements RowMapper<WatchProgress> {
    @Override
    public WatchProgress mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new WatchProgress(rs.getLong("video_id"), rs.getLong("user_id"),
                rs.getLong("time_played"), rs.getTimestamp("last_watch_date").toLocalDateTime());
    }
}
